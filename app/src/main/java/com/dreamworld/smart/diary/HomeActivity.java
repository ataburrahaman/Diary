package com.dreamworld.smart.diary;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamworld.smart.diary.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

import static android.text.TextUtils.isEmpty;
import static android.text.TextUtils.join;


public class HomeActivity extends AppCompatActivity {

   private Toolbar toolbar;
   private FloatingActionButton febbtn;
   private EditText note;
   private EditText title;
   private Button save;
   private DatabaseReference mDatabase;
   private RecyclerView recyclerView;
   private FirebaseAuth mauth;

   // For Update Input Fild

    private EditText noteUp;
    private EditText titleUp;
    private Button btnDeleteUp;
    private Button btnUpdateup;

    //Store update variable

    public String noteSt;
    public String titleSt;
    public String post_Key;
    public String post_Key1;
    Shared sharePref;

    //read variable
    private TextView rdtitle;
    private TextView rdnote;
    private TextView canclebtn;
    private TextView editbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharePref=new Shared(this);

        if(sharePref.lodeNightModeState()==true)
        {
            setTheme(R.style.darktheme);
        }else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar=(Toolbar)findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your Story");

        mauth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mauth.getCurrentUser();

        String uId=mUser.getUid();

        mDatabase= FirebaseDatabase.getInstance().getReference().child("TaskNote").child(uId);

        mDatabase.keepSynced(true);


        recyclerView=findViewById(R.id.reclyar);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

       // recyclerView.smoothScrollToPosition(adapter.getItemCount());


        febbtn = (FloatingActionButton)findViewById(R.id.fab_btn);
        febbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder myDialog= new AlertDialog.Builder(HomeActivity.this);
                LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);

                View myview = inflater.inflate(R.layout.caustominputfild,null);
                myDialog.setView(myview);

                final AlertDialog dialog=myDialog.create();

                title=myview.findViewById(R.id.edit_title);
                note=myview.findViewById(R.id.edit_note);
                save=myview.findViewById(R.id.save_btn);

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mtitle=title.getText().toString().trim();
                        String mnote=note.getText().toString().trim();

                        if (TextUtils.isEmpty(mtitle) && TextUtils.isEmpty(mnote)){
                            Toast.makeText(getApplicationContext(),"All Fields Are Empty",Toast.LENGTH_LONG).show();
                            return;
                        }


                        String id =mDatabase.push().getKey();
                        String datee= DateFormat.getDateInstance().format(new Date());
                        Data data=new Data(mnote,mtitle,datee,id);
                        mDatabase.child(id).setValue(data);

                        Toast.makeText(getApplicationContext(),"Data Save Successfully",Toast.LENGTH_SHORT).show();

                        dialog.dismiss();

                    }
                });


                dialog.show();




            }
        });


    }

    @Override
    protected void onStart() {

        super.onStart();


        final FirebaseRecyclerAdapter<Data,MyViewholder>adapter=new FirebaseRecyclerAdapter<Data, MyViewholder>
                (
                        Data.class,
                        R.layout.item_data,
                        MyViewholder.class,
                        mDatabase
                ) {
            @Override
            protected void populateViewHolder(MyViewholder viewHolder, final Data model, final int position) {

               // recyclerView.add;
                //recyclerView.smoothScrollToPosition(position);


            //  viewHolder.mtitle.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));


               // viewHolder.dateLoade.setAnimation(AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_transition_animation));
                viewHolder.container.setAnimation(AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_scale_animation));

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDate(model.getDate());
                viewHolder.setNote(model.getNote());



               viewHolder.myView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                    try {
                        post_Key = getRef(position).getKey();
                        titleSt = model.getTitle();
                        noteSt = model.getNote();

                    /*
                        PopupMenu p = new PopupMenu(getApplicationContext(),v);
                        p.getMenuInflater().inflate(R.menu.longprassmenu,p.getMenu());
                        p.show();*/
                        registerForContextMenu(v);
                   }catch(Exception e){
                        mDatabase.keepSynced(true);
                        Toast.makeText(getApplicationContext(),".."+e,Toast.LENGTH_SHORT).show();
                    }


                        return false;
                    }
                });

                viewHolder.myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                      try {

                            post_Key = getRef(position).getKey();

                            titleSt = model.getTitle();
                            noteSt = model.getNote();
                          //updateData();

                            showDataView();
                     }catch (Exception e){
                          mDatabase.keepSynced(true);
                            Toast.makeText(getApplicationContext()," normal click",Toast.LENGTH_SHORT).show();
                        }

                       // updateData();
                    }
                });

            }

        };

        recyclerView.setAdapter(adapter);
    }


    public static class MyViewholder extends RecyclerView.ViewHolder{

        View myView;
        TextView mtitle;
        CardView container;
        RelativeLayout dateLoade;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            myView=itemView;
            container=itemView.findViewById(R.id.cardViewlode);
            dateLoade=itemView.findViewById(R.id.animationDate);
        }

        public void setTitle(String title){
            mtitle=myView.findViewById(R.id.edittitle);
            mtitle.setText(title);
        }

        public void setNote(String note){
            TextView mnote=myView.findViewById(R.id.editnote);
            mnote.setText(note);
        }
        private void setDate(String tdate){
            TextView mdate=myView.findViewById(R.id.editdate);
            mdate.setText(tdate);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mainmenu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    public void showDataView() {
        AlertDialog.Builder myshowDialog=new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater inflater=LayoutInflater.from(HomeActivity.this);

        View myview=inflater.inflate(R.layout.readonlyshownote,null);
        myshowDialog.setView(myview);

        final  AlertDialog myreadAlartDialog=myshowDialog.create();

        rdtitle=myview.findViewById(R.id.edit_title_show);
        rdnote=myview.findViewById(R.id.edit_note_show);
        editbtn=myview.findViewById(R.id.edit_btn);
        canclebtn=myview.findViewById(R.id.close_btn);

        rdtitle.setText(titleSt);
        rdnote.setText(noteSt);
       editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(),"titlt: ",Toast.LENGTH_SHORT).show();
               //Toast.makeText(getApplicationContext(),"Note: "+noteSt,Toast.LENGTH_SHORT).show();
               // Toast.makeText(getApplicationContext(),"Key: "+post_Key,Toast.LENGTH_SHORT);
                myreadAlartDialog.dismiss();
                updateData();

            }
        });


        canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myreadAlartDialog.dismiss();
            }
        });

        myreadAlartDialog.show();

    }

    public void updateData(){

        AlertDialog.Builder myDialoge= new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater inflater=LayoutInflater.from(HomeActivity.this);

        View myview=inflater.inflate(R.layout.updateinputfild,null);
        myDialoge.setView(myview);

        final AlertDialog myAlartdialog=myDialoge.create();

        titleUp=myview.findViewById(R.id.edit_title_update);
        noteUp=myview.findViewById(R.id.edit_note_update);

        titleUp.setText(titleSt);
        titleUp.setSelection(titleSt.length());

        noteUp.setText(noteSt);
        noteUp.setSelection(noteSt.length());


        btnDeleteUp=myview.findViewById(R.id.delete_btn);
        btnUpdateup=myview.findViewById(R.id.update_btn);

        btnUpdateup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                titleSt=titleUp.getText().toString().trim();
                noteSt=noteUp.getText().toString().trim();

                String mdate=DateFormat.getDateInstance().format(new Date());

                Data data=new Data(noteSt,titleSt,mdate,post_Key);

                mDatabase.child(post_Key).setValue(data);
              //  Snackbar.make(v,"Update Successfully",Snackbar.LENGTH_LONG).show();

               Toast.makeText(getApplicationContext(),"Update Successfully",Toast.LENGTH_SHORT).show();

                myAlartdialog.dismiss();
            }
        });

        btnDeleteUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // mDatabase.child(post_Key).removeValue();
                deleteItem();
                //Snackbar.make(v,"Delete Successfully",Snackbar.LENGTH_SHORT).show();



                //Toast.makeText(getApplicationContext(),"Delete Successfully",Toast.LENGTH_SHORT).show();

                myAlartdialog.dismiss();
            }
        });




        myAlartdialog.show();




    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.longprassmenu,menu);

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case R.id.edit:
                updateData();
                //Toast.makeText(getApplicationContext(),"Edit are Selected",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.share:
                shareItem();
               // Toast.makeText(getApplicationContext(),"Inside Share",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.delete:
                deleteItem();
                //Snackbar.make(MyViewholder,"HH J",Snackbar.LENGTH_SHORT).show();
                return true;
                default:
                    return super.onContextItemSelected(item);

        }



    }

    private void shareItem() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,noteSt+"\n\n\n"+"--Diary World");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, titleSt );
        startActivity(Intent.createChooser(shareIntent, "Share Via Diary Wold"));
    }

    private void deleteItem() {
        mDatabase.child(post_Key).removeValue();
       // finish();

        Intent intent=new Intent(HomeActivity.this,HomeActivity.class);



        /*overridePendingTransition(3,3);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        //overridePendingTransition(0,0);*/
        startActivity(intent);
        overridePendingTransition(0,0);

       /*Snackbar snackbar= Snackbar.make(CoordinatorLayout,"HH J",Snackbar.LENGTH_SHORT);
       snackbar.dismiss();
       */

        Toast.makeText(getApplicationContext(),"Item Delete",Toast.LENGTH_SHORT).show();
       // mDatabase.keepSynced(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.editlogou2:
                startActivity(new Intent(getApplicationContext(),SattingActivity.class));
                finish();
                break;


        }
        return super.onOptionsItemSelected(item);
    }
}

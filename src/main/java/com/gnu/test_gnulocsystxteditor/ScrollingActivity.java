/*
 * GNULOCSYS TXT EDITOR for Android
 *
 * Copyright (c) 2020 A.D.Klumpp
 *
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */



package com.gnu.test_gnulocsystxteditor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


public class ScrollingActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 000;


    //
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //to fix crash when minimizing.
        outState.clear();
    }
    String criteria = "ethics";
    String inputpath = "/Documents/readme.txt";





    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //


        getSupportActionBar().setDisplayShowTitleEnabled(false);




        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    //

    public void clearinputp()
    {
        EditText editText = (EditText) findViewById(R.id.pathtext);
        editText.setText("");
    }

    //counts search results
    public void SearchResults()
    {
        int searchresult = -1;
        final Context context = this;

   /*     System.out.println("debug output SearchResults() Criteria:");
        System.out.println(criteria);

        System.out.println("debug output Criteria:");
        System.out.println(criteria);*/

        // input pop up

        EditText tvtt = (EditText)findViewById(R.id.editTextTextMultiLine);

        //
        String fullText = tvtt.getText().toString();
        if (fullText.contains(criteria))

        {

            int results = 0;

            int indexOfCriteria = fullText.indexOf(criteria);
            int lineNumber = tvtt.getLayout().getLineForOffset(indexOfCriteria);

          /*  System.out.println("debug output indexOfCriteria:");
            System.out.println(indexOfCriteria);

            System.out.println("debug output lineNumber:");
            System.out.println(lineNumber);*/

//
            int howoften = 0;
            for (int index = fullText.indexOf(criteria);
                 index >= 0;
                 index = fullText.indexOf(criteria, index + 1))
            {
                howoften=howoften+1;
                System.out.println("debug output index:");
                System.out.println(index);

                searchresult=howoften;

                int lineNumber2 = tvtt.getLayout().getLineForOffset(index);


               /* System.out.println("debug output lineNumber:");
                System.out.println(lineNumber2);

                System.out.println("debug output how often...:");
                System.out.println(howoften);*/

            }



         /*   System.out.println("debug output howoften end:");
            System.out.println(howoften);*/


            Toast.makeText(context, "Results found: "+howoften, Toast.LENGTH_SHORT).show();


            tvtt.setSelection(indexOfCriteria);


        }
        else
        {

            Toast.makeText(context, "Not found", Toast.LENGTH_SHORT).show();

        }

    }


    //

    @SuppressLint("LongLogTag")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        int searchresult = -1;

        if (id == R.id.action_open)
        {

            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissions,PERMISSION_REQUEST_CODE);


            final File intStore = Environment.getExternalStorageDirectory();

            String pathstrng = intStore.toString();


            final Context context = this;


            final EditText edit =  (EditText) findViewById(R.id.pathtext);
            String Docupath = edit.getText().toString();


                    File file = new File(intStore,Docupath);

                    //permanent display of path to txt on top of app
                    TextView tvpath = (TextView) findViewById(R.id.pathtext);

//Read text from file
                    StringBuilder text = new StringBuilder();

                    try {
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        String line;
                        //int n = 0;
                        while ((line = br.readLine()) != null)
                        { //n=n+1;
                            text.append(line);
                            text.append('\n');
                        }
                        br.close();
                    }

                    catch (IOException e)
                    {

                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        String exceptionAsString = sw.toString();

                        tvpath.setText(exceptionAsString);

                    }



                    EditText tv = (EditText)findViewById(R.id.editTextTextMultiLine);

//Set the text
                    tv.setText(text);




        }

        //
        if (id == R.id.action_docu)
        {
            EditText editText = (EditText) findViewById(R.id.pathtext);
            editText.setText("/Documents/file.txt");
        }

        if (id == R.id.action_drive)
        {
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissions,PERMISSION_REQUEST_CODE);
            final Context context = this;
            final File intStore = Environment.getExternalStorageDirectory();

            final String filestrg = intStore.toString();

            Toast.makeText(context, filestrg , Toast.LENGTH_SHORT).show();

           /* runOnUiThread(new Runnable() {
                public void run()
                {
                    Toast.makeText(context, filestrg , Toast.LENGTH_SHORT).show();
                }
            });*/



        }



        if (id == R.id.action_save)
        {


            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissions,PERMISSION_REQUEST_CODE);


            final File intStore = Environment.getExternalStorageDirectory();


            String pathstrng = intStore.toString();


            final Context context = this;


            final EditText edit =  (EditText) findViewById(R.id.pathtext);
            String Docupath = edit.getText().toString();


            File file = new File(intStore,Docupath);


//get the text


                            final EditText editout = (EditText) findViewById(R.id.editTextTextMultiLine);
                            String txtfromview = editout.getText().toString();


                            try {
                                File root = new File(Environment.getExternalStorageDirectory(), "Documents");
                                if (!root.exists()) {
                                    root.mkdirs();
                                }
                                File gpxfile = new File(pathstrng, Docupath);
                                FileWriter writer = new FileWriter(gpxfile);
                                writer.append(txtfromview);
                                writer.flush();
                                writer.close();
                                Toast.makeText(context, Docupath+ " saved", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {


                                TextView tvpath = (TextView) findViewById(R.id.pathtext);

                                StringWriter sw = new StringWriter();
                                e.printStackTrace(new PrintWriter(sw));
                                String exceptionAsString = sw.toString();

                                tvpath.setText(exceptionAsString);


                            }


                        }


        if (id == R.id.action_remember)
        {


            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissions,PERMISSION_REQUEST_CODE);


            final File intStore = Environment.getExternalStorageDirectory();


            String pathstrng = intStore.toString();


            final Context context = this;

//get the text

            final EditText edit =  (EditText) findViewById(R.id.pathtext);
            String Docupath = edit.getText().toString();



            try {
                File root = new File(Environment.getExternalStorageDirectory(), "GNULOCSYS");
                if (!root.exists()) {
                    root.mkdirs();
                }
                File gpxfile = new File(root, "Saved_Path");
                FileWriter writer = new FileWriter(gpxfile);
                writer.append(Docupath);
                writer.flush();
                writer.close();
                Toast.makeText(context, "Path saved", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {


                TextView tvpath = (TextView) findViewById(R.id.pathtext);

                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();

                tvpath.setText(exceptionAsString);


            }


        }



        if (id == R.id.action_load)
        {




            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissions,PERMISSION_REQUEST_CODE);


            final File intStore = Environment.getExternalStorageDirectory();

            String pathstrng = intStore.toString();


           String Docupath = "/GNULOCSYS/Saved_Path";

            TextView tvpath = (TextView) findViewById(R.id.pathtext);

            final Context context = this;

                    File file = new File(intStore,Docupath);


//Read text from file
                    StringBuilder text = new StringBuilder();

                    try {
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        String line;
                        //int n = 0;
                        while ((line = br.readLine()) != null)
                        { //n=n+1;
                            text.append(line);

                        }
                        br.close();
                    }

                    catch (IOException e)
                    {

                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        String exceptionAsString = sw.toString();

                        tvpath.setText(exceptionAsString);


                    }




            tvpath.setText(text);



        }




        if (id == R.id.action_stats)
        {
            SearchResults();
        }





        if (id == R.id.action_search)
        {


            final Context context = this;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Find (with Find Next button):");

//  input
            final EditText input = new EditText(this);

            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
            builder.setView(input);

// buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    criteria = input.getText().toString();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.cancel();
                }
            });

            builder.show();



        }





        if (id == R.id.action_searchnext)
        {

            final Context context = this;


            EditText tvtt = (EditText)findViewById(R.id.editTextTextMultiLine);

            int curpos = tvtt.getSelectionStart();


            String fullText = tvtt.getText().toString();
            if (fullText.contains(criteria))
            {

                int results = 0;

                int indexOfCriteria = fullText.indexOf(criteria);
                int lineNumber = tvtt.getLayout().getLineForOffset(indexOfCriteria);

        /*        System.out.println("debug output indexOfCriteria:");
                System.out.println(indexOfCriteria);

                System.out.println("debug output lineNumber:");
                System.out.println(lineNumber);*/


int counterp=0;
                int howoften = 0;
                for (int index = fullText.indexOf(criteria);
                     index >= 0;
                     index = fullText.indexOf(criteria, index + 1))
                {
                    howoften=howoften+1;

                /*    System.out.println("debug output index:");
                    System.out.println(index);*/

                    searchresult=howoften;

                    int lineNumber2 = tvtt.getLayout().getLineForOffset(index);

                    if(index>curpos)
                    {
                        tvtt.setSelection(index);
                        counterp=howoften;
                        break;
                    }


                    /*System.out.println("debug outputlineNumber:");
                    System.out.println(lineNumber2);

                    System.out.println("debug output howoften:");
                    System.out.println(howoften);*/

                }


               /* System.out.println("debug output howoften wnd:");
                System.out.println(howoften);*/


                Toast.makeText(context, "Result "+counterp, Toast.LENGTH_SHORT).show();




            }
            else
            {

                Toast.makeText(context, "Not found", Toast.LENGTH_SHORT).show();

            }

        }



        if (id == R.id.action_read)
        {

           EditText view = (EditText)findViewById(R.id.editTextTextMultiLine);
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            view.setShowSoftInputOnFocus(false);


        }


        if (id == R.id.action_edit)
        {

            EditText view = (EditText)findViewById(R.id.editTextTextMultiLine);
            view.setShowSoftInputOnFocus(true);


        }

        if (id == R.id.action_top)
        {

            EditText view = (EditText)findViewById(R.id.editTextTextMultiLine);
            view.setShowSoftInputOnFocus(false);
            view.setSelection(0);


        }

        if (id == R.id.action_bottom)
        {

            EditText view = (EditText)findViewById(R.id.editTextTextMultiLine);
            view.setShowSoftInputOnFocus(false);
            view.setSelection(view.getText().length());




        }

        if (id == R.id.action_goto)
        {

            EditText view = (EditText)findViewById(R.id.editTextTextMultiLine);
            view.setShowSoftInputOnFocus(false);

            int pos =  view.getText().length();
            int per = 50;
            int two = 100/per;
            view.setSelection(pos/two);




        }


        if (id == R.id.action_plus10)
        {

            EditText view = (EditText) findViewById(R.id.editTextTextMultiLine);
            view.setShowSoftInputOnFocus(false);

            int curpos = view.getSelectionStart();

            int pos = view.getText().length();
        /*    int per = 10;
            int two = 100/per;*/
            int delt10 = pos/10;
            int curdest = curpos + delt10;

            if (curdest < pos) {
                view.setSelection(curdest);

            }
            if (curdest > pos) {
                view.setSelection(pos);

            }


        }


        if (id == R.id.action_minus10)
        {

            EditText view = (EditText) findViewById(R.id.editTextTextMultiLine);
            view.setShowSoftInputOnFocus(false);

            int curpos = view.getSelectionStart();

            int pos = view.getText().length();
        /*    int per = 10;
            int two = 100/per;*/
            int delt10 = pos/10;
            int curdest = curpos - delt10;

            if (curdest < 0) {
                view.setSelection(0);

            }
            if (curdest > 0) {
                view.setSelection(curdest);

            }


        }



        return super.onOptionsItemSelected(item);
    }
}
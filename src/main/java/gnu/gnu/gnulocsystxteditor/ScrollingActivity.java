/* * GNULOCSYS TXT EDITOR for Android
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
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA*/



/*
to do
-
-
-
to do end
*/

/*
changelog
    v59
        - layout change: more space under menu button
        - scrollbar size changed from 20 to 16dp outside overlay

        - removed: List txt /Documents (java and layout)
        - removed List 0 directory
        - removed example: /Documents/file.txt

        - full path input
        - [Input]-> command
        - set dir -> [Input]
        - [Input]-> List dir
        - [Input]-> Clear
        - List / dir
        - List /storage dir
        - About
     v61
        - save new line in bookmark file
        - move all non-toast error messages to toast
     v63
        - version number included in 'About'
        - search .toLowerCase()
     v65
        - list all files and folders as tree to search for files..
     v67
        - txt file filter
        - removed: System.out.println
     v69
        - requestPermissions removed for (Build.VERSION.SDK_INT < 23)

changelog end
*/

package gnu.gnu.gnulocsystxteditor;

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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import java.io.Writer;


public class ScrollingActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 000;


    //
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //to fix crash when minimizing.
        outState.clear();
    }

    String version = "v69";

    String criteria = "ethics";
    String inputpath = "/Documents/file.txt";





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


    //get tree
    public void treefunction(String path, List<String> list)
    {
        File directory = new File(path);


        File[] filerlist = directory.listFiles();

        if (filerlist != null)
        {
            for (File file : filerlist)
            {
                if (file.isFile())
                {

                    list.add(file.getAbsolutePath());
                }

                else if (file.isDirectory())
                {
                    treefunction(file.getAbsolutePath(), list);
                }
            }
        }

        else

        {
            final Context context = this;
            Toast.makeText(context, "Empty directory", Toast.LENGTH_SHORT).show();
        }


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
        if (fullText.toLowerCase().contains(criteria.toLowerCase()))

        {

            int results = 0;

            int indexOfCriteria = fullText.toLowerCase().indexOf(criteria.toLowerCase());
            int lineNumber = tvtt.getLayout().getLineForOffset(indexOfCriteria);

          /*  System.out.println("debug output indexOfCriteria:");
            System.out.println(indexOfCriteria);

            System.out.println("debug output lineNumber:");
            System.out.println(lineNumber);*/

//
            int howoften = 0;
            for (int index = fullText.toLowerCase().indexOf(criteria.toLowerCase());
                 index >= 0;
                 index = fullText.toLowerCase().indexOf(criteria.toLowerCase(), index + 1))
            {
                howoften=howoften+1;
               /* System.out.println("debug output index:");
                System.out.println(index);*/

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
            if (Build.VERSION.SDK_INT >= 23) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }

           //final File intStore = Environment.getExternalStorageDirectory();

            //String pathstrng = intStore.toString();


            final Context context = this;

                        EditText tv = (EditText)findViewById(R.id.editTextTextMultiLine);
                         //TextView tver = (TextView) findViewById(R.id.editTextTextMultiLine);
                        
            final EditText edit =  (EditText) findViewById(R.id.pathtext);
           String Docupath = edit.getText().toString();
            //String Docupath = "/Documents/8001.txt";


                   // File file = new File(intStore,Docupath);

            File file = new File(Docupath);

                    //permanent display of path to txt on top of app
                    //TextView tvpath = (TextView) findViewById(R.id.pathtext);
           // tvpath.setText(pathstrng+Docupath);

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

                       // tv.setText(exceptionAsString);


                         Toast.makeText(context, exceptionAsString, Toast.LENGTH_LONG).show();
                    }





//Set the text
                    tv.setText(text);




        }

        //
        if (id == R.id.action_clear)
        {
            EditText editText = (EditText) findViewById(R.id.pathtext);
            editText.setText("");
        }

        if (id == R.id.action_drive)
        {

            if (Build.VERSION.SDK_INT >= 23)
            {

            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissions,PERMISSION_REQUEST_CODE);
            }

            final Context context = this;
            final File intStore = Environment.getExternalStorageDirectory();

            final String filestrg = intStore.toString();

            //Toast.makeText(context, filestrg , Toast.LENGTH_SHORT).show();


            final EditText edit =  (EditText) findViewById(R.id.pathtext);
            edit.setText(filestrg);

           /* runOnUiThread(new Runnable() {
                public void run()
                {
                    Toast.makeText(context, filestrg , Toast.LENGTH_SHORT).show();
                }
            });*/



        }

        if (id == R.id.action_root)
        {
            if (Build.VERSION.SDK_INT >= 23) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }
           /* final Context context = this;
            final File intStore = Environment.getExternalStorageDirectory();

            final String filestrg = intStore.toString();
*/
            //Toast.makeText(context, filestrg , Toast.LENGTH_SHORT).show();


            final EditText edit =  (EditText) findViewById(R.id.pathtext);
            edit.setText("/");

           /* runOnUiThread(new Runnable() {
                public void run()
                {
                    Toast.makeText(context, filestrg , Toast.LENGTH_SHORT).show();
                }
            });*/



        }

        if (id == R.id.action_storage)
        {
            if (Build.VERSION.SDK_INT >= 23) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }
           /* final Context context = this;
            final File intStore = Environment.getExternalStorageDirectory();

            final String filestrg = intStore.toString();
*/
            //Toast.makeText(context, filestrg , Toast.LENGTH_SHORT).show();


            final EditText edit =  (EditText) findViewById(R.id.pathtext);
            edit.setText("/storage");

           /* runOnUiThread(new Runnable() {
                public void run()
                {
                    Toast.makeText(context, filestrg , Toast.LENGTH_SHORT).show();
                }
            });*/



        }



        if (id == R.id.action_save)
        {

            if (Build.VERSION.SDK_INT >= 23) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }


            //final File intStore = Environment.getExternalStorageDirectory();


            //String pathstrng = intStore.toString();


            final Context context = this;


            final EditText edit =  (EditText) findViewById(R.id.pathtext);
            String Docupath = edit.getText().toString();


            File file = new File(Docupath);


//get the text


                            final EditText editout = (EditText) findViewById(R.id.editTextTextMultiLine);
                            String txtfromview = editout.getText().toString();


                            try {
                                /*File root = new File(Environment.getExternalStorageDirectory(), "Documents");
                                if (!root.exists())
                                {
                                    root.mkdirs();
                                }*/
                                File gpxfile = new File(Docupath);
                                FileWriter writer = new FileWriter(gpxfile);
                                writer.append(txtfromview);
                                writer.flush();
                                writer.close();
                                Toast.makeText(context, Docupath+ " saved", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {


                                //TextView tvpath = (TextView) findViewById(R.id.pathtext);

                                StringWriter sw = new StringWriter();
                                e.printStackTrace(new PrintWriter(sw));
                                String exceptionAsString = sw.toString();

                                //editout.setText(exceptionAsString);


                                 Toast.makeText(context, exceptionAsString, Toast.LENGTH_LONG).show();

                            }


                        }


        if (id == R.id.action_remember)
        {

            if (Build.VERSION.SDK_INT >= 23) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }


            final File intStore = Environment.getExternalStorageDirectory();


            String pathstrng = intStore.toString();


            final Context context = this;

//get the text

            final EditText edit =  (EditText) findViewById(R.id.pathtext);
            String Docupath = edit.getText().toString();

//mod


            Writer output;






            //

            try {
                File root = new File(Environment.getExternalStorageDirectory(), "GNULOCSYS");
                if (!root.exists()) {
                    root.mkdirs();
                }



                File gpxfile = new File(root, "Saved_Path");
//

                output = new BufferedWriter(new FileWriter(gpxfile, true));
                //FileWriter(gpxfile, boolean append);

                //output.append("New Line!");
                output.append(Docupath+"\n\n");

               // output.append("\n\n");
                //
                output.close();



                //
            /*    FileWriter writer = new FileWriter(gpxfile);
                writer.append(Docupath);
                writer.flush();
                writer.close();*/




            //old
           /* try {
                File root = new File(Environment.getExternalStorageDirectory(), "GNULOCSYS");
                if (!root.exists()) {
                    root.mkdirs();
                }
                File gpxfile = new File(root, "Saved_Path");


                FileWriter writer = new FileWriter(gpxfile);
                writer.append(Docupath);
                writer.flush();
                writer.close();*/


                //end old

                Toast.makeText(context, "Path saved", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {

                EditText tvtt = (EditText)findViewById(R.id.editTextTextMultiLine);
                //TextView tvpath = (TextView) findViewById(R.id.pathtext);

                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();

                //tvtt.setText(exceptionAsString);

               
                Toast.makeText(context, exceptionAsString, Toast.LENGTH_LONG).show();

            }


        }



        if (id == R.id.action_loadbookmarks)
        {



            if (Build.VERSION.SDK_INT >= 23) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }

            EditText tvtt = (EditText)findViewById(R.id.editTextTextMultiLine);
            final File intStore = Environment.getExternalStorageDirectory();

            String pathstrng = intStore.toString();


           String Docupath = "/GNULOCSYS/Saved_Path";

            TextView tvpath = (TextView) findViewById(R.id.pathtext);

            final Context context = this;

                    File file = new File(intStore,Docupath);

            tvpath.setText(pathstrng+Docupath);


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
                        //EditText tvtt = (EditText)findViewById(R.id.editTextTextMultiLine);
                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        String exceptionAsString = sw.toString();

                       // tvtt.setText(exceptionAsString);


                        Toast.makeText(context, exceptionAsString, Toast.LENGTH_LONG).show();

                    }




            tvtt.setText(text);



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
           // if (fullText.contains(criteria))
                if (fullText.toLowerCase().contains(criteria.toLowerCase()))
            {

                int results = 0;

                int indexOfCriteria = fullText.toLowerCase().indexOf(criteria.toLowerCase());
                int lineNumber = tvtt.getLayout().getLineForOffset(indexOfCriteria);

        /*        System.out.println("debug output indexOfCriteria:");
                System.out.println(indexOfCriteria);

                System.out.println("debug output lineNumber:");
                System.out.println(lineNumber);*/


int counterp=0;
                int howoften = 0;
                for (int index = fullText.toLowerCase().indexOf(criteria.toLowerCase());
                     index >= 0;
                     index = fullText.toLowerCase().indexOf(criteria.toLowerCase(), index + 1))
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
/*

        if (id == R.id.action_listdocu)
        {

            EditText editText = (EditText) findViewById(R.id.pathtext);
            editText.setText("");

            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissions,PERMISSION_REQUEST_CODE);

            EditText view = (EditText)findViewById(R.id.editTextTextMultiLine);
            view.setShowSoftInputOnFocus(false);





            String path = Environment.getExternalStorageDirectory().toString()+"/Documents";

            File directory = new File(path);
            File[] files = directory.listFiles();
            List<String> list = new ArrayList<>();

            if(directory.listFiles()!=null) {

            for (int i = 0; i < files.length; i++)
            {
                if((files[i].getName()).toLowerCase().contains(".txt")) {


                    list.add(files[i].getName());
                    //list.add("\n\n");

                    System.out.println("files[i].getName()-----------------------------");
                    System.out.println(files[i].getName());
                }


            }


            Collections.sort(list, String.CASE_INSENSITIVE_ORDER);

            //

            StringBuilder builder = new StringBuilder();
            for (String value : list)
            {
                builder.append("/Documents/"+value);
                builder.append("\n\n");
            }
            String text = builder.toString();


//
       */
/*     Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
            view.setText(list.toString());*//*



            view.setText(text);

            }

            else

            {
                final Context context = this;
                Toast.makeText(context, "Empty directory", Toast.LENGTH_SHORT).show();
            }






        }
*/

        //
/*

        if (id == R.id.action_listall)
        {

            EditText editText = (EditText) findViewById(R.id.pathtext);
            editText.setText("");

            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissions,PERMISSION_REQUEST_CODE);

            EditText view = (EditText)findViewById(R.id.editTextTextMultiLine);
            view.setShowSoftInputOnFocus(false);





            String path = Environment.getExternalStorageDirectory().toString();

            File directory = new File(path);
            File[] files = directory.listFiles();
            List<String> list = new ArrayList<>();

            if(directory.listFiles()!=null) {

            for (int i = 0; i < files.length; i++)
            {
                */
/*if((files[i].getName()).toLowerCase().contains(".txt"))

                {*//*



                    list.add(files[i].getName());
                    //list.add("\n\n");

                    System.out.println("files[i].getName()-----------------------------");
                    System.out.println(files[i].getName());
                //}


            }


            Collections.sort(list, String.CASE_INSENSITIVE_ORDER);

            //

            StringBuilder builder = new StringBuilder();
            for (String value : list)
            {
                //builder.append("/Documents/"+value);
                builder.append("/"+value);
                builder.append("\n\n");
            }
            String text = builder.toString();


//
       */
/*     Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
            view.setText(list.toString());*//*



            view.setText(text);


            }

            else

            {
                final Context context = this;
                Toast.makeText(context, "Empty directory", Toast.LENGTH_SHORT).show();
            }





        }
*/


/*

        if (id == R.id.action_listcommand)
        {

            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissions,PERMISSION_REQUEST_CODE);

            EditText view = (EditText)findViewById(R.id.editTextTextMultiLine);
            view.setShowSoftInputOnFocus(false);

            final EditText editi =  (EditText) findViewById(R.id.pathtext);
            String inputpath = editi.getText().toString();





            String drivepath = Environment.getExternalStorageDirectory().toString();

            String path = drivepath  + inputpath;



            File directory = new File(path);
            File[] files = directory.listFiles();

            if(directory.listFiles()!=null) {

                List<String> list = new ArrayList<>();

                for (int i = 0; i < files.length; i++) {
                */
/*if((files[i].getName()).toLowerCase().contains(".txt"))

                {*//*



                    list.add(files[i].getName());
                    //list.add("\n\n");

                    System.out.println("files[i].getName()-----------------------------");
                    System.out.println(files[i].getName());
                    //}


                }


                Collections.sort(list, String.CASE_INSENSITIVE_ORDER);

                //

                StringBuilder builder = new StringBuilder();
                for (String value : list) {
                    //builder.append("/Documents/"+value);
                    builder.append(inputpath + "/" + value);
                    builder.append("\n\n");
                }
                String text = builder.toString();


//
       */
/*     Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
            view.setText(list.toString());*//*



                view.setText(text);


            }

            else

            {
                final Context context = this;
                Toast.makeText(context, "Empty directory", Toast.LENGTH_SHORT).show();
            }





        }
*/

//list only the first level of dir
        if (id == R.id.action_listdir)
        {
            if (Build.VERSION.SDK_INT >= 23) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }

            EditText view = (EditText)findViewById(R.id.editTextTextMultiLine);
            view.setShowSoftInputOnFocus(false);

            final EditText editi =  (EditText) findViewById(R.id.pathtext);
            String inputpath = editi.getText().toString();

           // String drivepath = Environment.getExternalStorageDirectory().toString();

            String path = inputpath;

            File directory = new File(path);
            File[] files = directory.listFiles();

            if(directory.listFiles()!=null) {

                List<String> list = new ArrayList<>();

                for (int i = 0; i < files.length; i++) {
                /*if((files[i].getName()).toLowerCase().contains(".txt"))

                {*/


                    //list.add(files[i].getName());
                    list.add(files[i].getAbsolutePath());
                    //list.add("\n\n");

                 /*   System.out.println("files[i].getName()-----------------------------");
                    System.out.println(files[i].getName());*/
                    //}


                }


                Collections.sort(list, String.CASE_INSENSITIVE_ORDER);

                //

                StringBuilder builder = new StringBuilder();
                for (String value : list) {
                    //builder.append("/Documents/"+value);
                  // builder.append(inputpath + "/" + value);
                     builder.append(value);
                    builder.append("\n\n");
                }
                String text = builder.toString();


                view.setText(text);


            }

            else

            {
                final Context context = this;
                Toast.makeText(context, "Empty directory", Toast.LENGTH_SHORT).show();
            }



        }



//list all subfolders/files of dir
        if (id == R.id.action_tree)
        {
            if (Build.VERSION.SDK_INT >= 23) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }


            EditText view = (EditText)findViewById(R.id.editTextTextMultiLine);
            view.setShowSoftInputOnFocus(false);

            final EditText editi =  (EditText) findViewById(R.id.pathtext);
            String inputpath = editi.getText().toString();




            String path = inputpath;


            List<String> list = new ArrayList<>();
            treefunction(inputpath, list);


                Collections.sort(list, String.CASE_INSENSITIVE_ORDER);



                StringBuilder builder = new StringBuilder();
                for (String value : list) {

                    builder.append(value);
                    builder.append("\n\n");
                }
                String text = builder.toString();





                view.setText(text);

        }



//txt filter
        if (id == R.id.action_treetxt)
        {

            if (Build.VERSION.SDK_INT >= 23) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }




            EditText view = (EditText)findViewById(R.id.editTextTextMultiLine);
            view.setShowSoftInputOnFocus(false);

            final EditText editi =  (EditText) findViewById(R.id.pathtext);
            String inputpath = editi.getText().toString();



            String path = inputpath;


            List<String> list = new ArrayList<>();
            treefunction(inputpath, list);



            Collections.sort(list, String.CASE_INSENSITIVE_ORDER);



            StringBuilder builder = new StringBuilder();
            for (String value : list)
            {
                if(value.contains(".txt")) {
                    builder.append(value);
                    builder.append("\n\n");
                }
            }
            String text = builder.toString();



            view.setText(text);



        }

// end txt filter


        if (id == R.id.action_about)
        {



            if (Build.VERSION.SDK_INT >= 23)
            {



                final Context context = this;
                Toast.makeText(context, "Build.VERSION.SDK_INT >= 23", Toast.LENGTH_LONG).show();

                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);





            }






            EditText view = (EditText)findViewById(R.id.editTextTextMultiLine);
            view.setShowSoftInputOnFocus(false);







                String text = "Version: "+version+"\n" +
                        "\n" +"GNULOCSYS TXT EDITOR for Android\n" +
                        "\n" +
                        "Copyright (c) 2020 A.D.Klumpp\n" +
                        " \n" +
                        "This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.\n" +
                        " \n" +
                        "This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.\n" +
                        " \n" +
                        "You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA";



                view.setText(text);







//--
        }




        return super.onOptionsItemSelected(item);
    }
}
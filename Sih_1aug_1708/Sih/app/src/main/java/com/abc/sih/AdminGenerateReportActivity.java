package com.abc.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

//

//

public class AdminGenerateReportActivity extends AppCompatActivity {

    DatabaseReference ref;
    String dirpath;
    TextView created;
    ArrayList<ReportAdminHelperClass> tablelist;
    Button repo_gen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_generate_report);

        ref = FirebaseDatabase.getInstance().getReference().child("Posts");
        ref.keepSynced(true);
        created = (TextView) findViewById(R.id.created);
        repo_gen = (Button) findViewById(R.id.repo_gen);
        repo_gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatepdf();
            }
        });


    }

    public void generatepdf(){

        Document mDoc = new Document();

        String mFileName = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis());

        String mFilePath = android.os.Environment.getExternalStorageDirectory()+ File.separator+"SIHCaseDir" + "/" + mFileName + ".pdf";

        try {
            PdfWriter.getInstance(mDoc,new FileOutputStream(mFilePath));
//
            Document document = new Document(PageSize.A4);
            PdfPTable table = new PdfPTable(new float[]{3, 3, 3, 3, 3});
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setFixedHeight(50);
            table.setTotalWidth(PageSize.A4.getWidth());
            table.setWidthPercentage(100);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell("Name");
            table.addCell("Geo_lat");
            table.addCell("Geo_long");
            table.addCell("Upd_name");
            table.addCell("Date");
            table.setHeaderRows(1);
            PdfPCell[] cells = table.getRow(0).getCells();
            for (int j = 0; j < cells.length; j++) {
                cells[j].setBackgroundColor(BaseColor.GRAY);
            }
            //
            mDoc.open();
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        if(ds.child("type").getValue()!=null && ds.child("geo_lat").getValue()!=null &&
                        ds.child("geo_long").getValue()!=null && ds.child("uploader_name").getValue()!=null &&
                        ds.child("time_date_case").getValue()!=null){
                            String case_title = ds.child("type").getValue().toString();
                            String Geo_lat = ds.child("geo_lat").getValue().toString();
                            String Geo_long = ds.child("geo_long").getValue().toString();
                            String upd_name = ds.child("uploader_name").getValue().toString();
                            String time_date = ds.child("time_date_case").getValue().toString();


                            table.addCell(case_title);
                            table.addCell(Geo_lat);
                            table.addCell(Geo_long);
                            table.addCell(upd_name);
                            table.addCell(time_date);

                            try {

                                //mDoc.add(new Paragraph());
                                //   mDoc.add(new Paragraph(case_title + ""+Geo_lat+""+Geo_long
                                //  +""+upd_name+""+time_date ));
                            } /*catch (DocumentException e) {

                            e.printStackTrace();
                        }*/ catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    }
                    try {
                        mDoc.add(table);
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                    mDoc.close();
                    Toast.makeText(AdminGenerateReportActivity.this, mFileName+".pdf\n is saved to\n"+mFilePath, Toast.LENGTH_SHORT).show();
                    created.setText(mFileName+".pdf\n is saved to\n"+mFilePath);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            //mDoc.close();
        }catch (Exception e){

            Toast.makeText(AdminGenerateReportActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }





    }

    public void layoutToImage() {
        // get view group using reference
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.main_print);
        // convert view group to bitmap
        relativeLayout.setDrawingCacheEnabled(true);
        relativeLayout.buildDrawingCache();
        Bitmap bm = relativeLayout.getDrawingCache();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        File f = new File(android.os.Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void imageToPDF() throws FileNotFoundException {
        try {
            Document document = new Document();
            dirpath = android.os.Environment.getExternalStorageDirectory() + File.separator+"SIHCaseDir";
            PdfWriter.getInstance(document, new FileOutputStream(dirpath + "/NewPDF.pdf")); //  Change pdf's name.
            document.open();
            Image img = Image.getInstance(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - 0) / img.getWidth()) * 100;
            img.scalePercent(scaler);
            img.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
            document.add(img);
            document.close();
            Toast.makeText(this, "PDF Generated successfully!..", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }
    }
}
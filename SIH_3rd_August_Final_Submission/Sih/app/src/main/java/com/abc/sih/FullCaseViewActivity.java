package com.abc.sih;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class FullCaseViewActivity extends AppCompatActivity {

    String post_id;
    DatabaseReference post_database;

    TextView user_profile_name,specie_text,specie_detail,upload_time,case_type_details,case_report_details,case_time_details,case_assigned_details;

    TextView case_urgency_details,uploader_name,uploader_mobile,uploader_email;
    TextView geolng_details,geolat_details;
    CircleImageView user_image;
    ImageView case_image,case_image2,case_image3,uploader_sign;
    LinearLayout other_images;

    String user_name,upd_time,user_dp,case_link,case_link2,case_link3,case_type,case_info,case_time_det,case_assgn_det;
    String case_urg_det,upd_name,upd_mobile,upd_email,upd_circlecode,upd_sign,upd_geolong,upd_geolat;

    String dirpath;
    Button pdf;
    Bitmap bmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_case_view);

        post_id = getIntent().getStringExtra("post_id");
        //Toast.makeText(FullCaseViewActivity.this,post_id,Toast.LENGTH_SHORT).show();
        //////
        user_profile_name = (TextView) findViewById(R.id.user_profile_name);
        upload_time = (TextView) findViewById(R.id.upload_time);
        user_image = (CircleImageView) findViewById(R.id.user_profile_image);
        case_image = (ImageView) findViewById(R.id.case_image);
        case_image2 = (ImageView) findViewById(R.id.case_image2);
        specie_detail = findViewById(R.id.species_type_detail);
        specie_text = findViewById(R.id.specie_type_text);
        case_image3 = (ImageView) findViewById(R.id.case_image3);
        other_images = (LinearLayout) findViewById(R.id.other_images);
        case_type_details = (TextView) findViewById(R.id.case_type_details);
        case_report_details = (TextView) findViewById(R.id.case_report_details);
        case_time_details = (TextView) findViewById(R.id.case_time_details);
        case_assigned_details = (TextView) findViewById(R.id.case_assigned_details);
        case_urgency_details = (TextView) findViewById(R.id.case_urgency_details);
        uploader_name = (TextView) findViewById(R.id.uploader_name);
        uploader_mobile = (TextView) findViewById(R.id.uploader_mobile);
        uploader_email = (TextView) findViewById(R.id.uploader_email);
        uploader_sign = (ImageView) findViewById(R.id.uploader_sign);
        pdf = (Button) findViewById(R.id.pdfbtn);
        geolng_details = (TextView) findViewById(R.id.case_geolong_details);
        geolat_details = (TextView) findViewById(R.id.case_geolat_details);
        //////

        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*layoutToImage();


                try {
                    imageToPDF();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }*/

                generatepdf();

            }
        });
        post_database = FirebaseDatabase.getInstance().getReference().child("Posts").child(post_id);

        post_database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_name = dataSnapshot.child("user_name").getValue().toString();
                Long h = dataSnapshot.child("time").getValue(Long.class);
                GetTimeAgo getTimeAgo = new GetTimeAgo();
                upd_time = getTimeAgo.getTimeAgo(h);

                user_dp = dataSnapshot.child("image").getValue().toString();
                case_link = dataSnapshot.child("link").getValue().toString();
                if(dataSnapshot.child("link1").exists())
                {
                    case_link2 = dataSnapshot.child("link1").getValue().toString();
                    Picasso.with(FullCaseViewActivity.this).load(case_link2).placeholder(R.drawable.default_avatar)
                            .networkPolicy(NetworkPolicy.OFFLINE).into(case_image2, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                            Picasso.with(FullCaseViewActivity.this).load(case_link2).placeholder(R.drawable.default_avatar).into(case_image2);
                        }
                    });
                }
                else{
                    case_image2.setVisibility(View.INVISIBLE);
                }
                if(dataSnapshot.child("link2").exists())
                {
                    case_link3 = dataSnapshot.child("link2").getValue().toString();
                    Picasso.with(FullCaseViewActivity.this).load(case_link3).placeholder(R.drawable.default_avatar)
                            .networkPolicy(NetworkPolicy.OFFLINE).into(case_image3, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                            Picasso.with(FullCaseViewActivity.this).load(case_link3).placeholder(R.drawable.default_avatar).into(case_image3);
                        }
                    });
                }
                else{
                    case_image3.setVisibility(View.INVISIBLE);
                }
                if(!dataSnapshot.child("link1").exists() && !dataSnapshot.child("link2").exists())
                {
                    other_images.setVisibility(View.GONE);
                }
                if (!dataSnapshot.child("species").getValue().toString().equals("N.A")){
                    String sp = dataSnapshot.child("species").getValue().toString();
                    specie_text.setVisibility(View.VISIBLE);
                    specie_detail.setVisibility(View.VISIBLE);
                    specie_detail.setText(sp);
                }else {
                    String sp = dataSnapshot.child("species").getValue().toString();
                    specie_detail.setText(sp);
                    specie_text.setVisibility(View.GONE);
                    specie_detail.setVisibility(View.GONE);
                }
                case_type = dataSnapshot.child("type").getValue().toString();
                case_info = dataSnapshot.child("case_details").getValue().toString();
                case_time_det = dataSnapshot.child("time_date_case").getValue().toString();
                case_assgn_det = dataSnapshot.child("case_assigned").getValue().toString();
                if(case_assgn_det.toLowerCase().equals("yes"))
                {
                    case_assigned_details.setText("YES");
                    case_assigned_details.setTextColor(Color.GREEN);
                }
                if(case_assgn_det.toLowerCase().equals("no"))
                {
                    case_assigned_details.setText("NO");
                    case_assigned_details.setTextColor(Color.RED);
                }
                case_urg_det = dataSnapshot.child("likes").getValue().toString();
                upd_name = dataSnapshot.child("uploader_name").getValue().toString();
                upd_mobile = dataSnapshot.child("mobile").getValue().toString();
                upd_email = dataSnapshot.child("email").getValue().toString();
                upd_geolong = dataSnapshot.child("geo_long").getValue().toString();
                upd_geolat = dataSnapshot.child("geo_lat").getValue().toString();
                if (dataSnapshot.child("signature_link").exists())
                {

                    Log.e("nisi","inside");
                    /*
                     Picasso.with(context)
                    .load(Uri.parse(getItem(position).getStoryBigThumbUrl()))
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(holder.storyBigThumb, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            // Try again online if cache failed
                            Picasso.with(context)
                                    .load(Uri.parse(getItem(position)
                                            .getStoryBigThumbUrl()))
                            .placeholder(R.drawable.user_placeholder)
                            .error(R.drawable.user_placeholder_error)
                                    .into(holder.storyBigThumb);
                        }
                    });
                     */
                    upd_sign = dataSnapshot.child("signature_link").getValue().toString();
                    Picasso.with(FullCaseViewActivity.this).load(upd_sign).placeholder(R.drawable.default_avatar)
                            .networkPolicy(NetworkPolicy.OFFLINE).into(uploader_sign, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                            Picasso.with(FullCaseViewActivity.this).load(upd_sign).into(uploader_sign);
                        }
                    });
                }



                Log.e("anish_sign",upd_sign);
                Log.e("anish_name",user_name);
                user_profile_name.setText(user_name);
                upload_time.setText(upd_time);
                Picasso.with(FullCaseViewActivity.this).load(user_dp).placeholder(R.drawable.default_avatar)
                        .networkPolicy(NetworkPolicy.OFFLINE).into(user_image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                        Picasso.with(FullCaseViewActivity.this).load(user_dp).placeholder(R.drawable.default_avatar).into(user_image);
                    }
                });
                Picasso.with(FullCaseViewActivity.this).load(case_link).placeholder(R.drawable.default_avatar)
                        .networkPolicy(NetworkPolicy.OFFLINE).into(case_image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                        Picasso.with(FullCaseViewActivity.this).load(case_link).placeholder(R.drawable.default_avatar).into(case_image);
                    }
                });
                case_type_details.setText(case_type);
                case_report_details.setText(case_info);
                case_time_details.setText(case_time_det);
                case_urgency_details.setText(case_urg_det);
                uploader_name.setText(upd_name);
                uploader_mobile.setText(upd_mobile);
                uploader_email.setText(upd_email);
                geolng_details.setText(upd_geolong);
                geolat_details.setText(upd_geolat);
                //uploader_circlecode.setText(upd_circlecode);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void generatepdf(){
        Document mDoc = new Document();

        String mFileName = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis());

        String mFilePath = android.os.Environment.getExternalStorageDirectory()+ File.separator+"SIHCaseDir" + "/" + mFileName + ".pdf";

        try{
            PdfWriter.getInstance(mDoc,new FileOutputStream(mFilePath));

            mDoc.open();

            String mText2 = user_profile_name.getText().toString();
            String mText1 = post_id;
            String mText3 = case_type_details.getText().toString();
            String mText4 = case_time_details.getText().toString();
            String mText5 = case_report_details.getText().toString();
            String mText6 = case_assigned_details.getText().toString();
            String mText7 = case_urgency_details.getText().toString();
            String mText8 = uploader_mobile.getText().toString();
            String mText9 = uploader_email.getText().toString();
            String mText10 = specie_detail.getText().toString();
            String mText11 = case_link;
            String mText12 = geolat_details.getText().toString();
            String mText13 = geolng_details.getText().toString();

            //bmp = BitmapFactory.decodeResource(getResources(),)

            //user_profile_name.getDrawable();
            //Image img1 = (Image) findViewById(R.id.user_profile_image);
            //String mText2 = user_

            mDoc.addAuthor("Anish Adnani");

            Font blue = new Font(Font.FontFamily.HELVETICA, 16, Font.NORMAL, BaseColor.BLUE);
            Font black = new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL, BaseColor.BLACK);
            Font green = new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL, BaseColor.GREEN);
            Font red = new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL, BaseColor.RED);

            Chunk blueText1 = new Chunk(mText1, black);
            Chunk blueText2 = new Chunk(mText2,black);
            Chunk blueText3 = new Chunk(mText3, black);
            Chunk blueText4 = new Chunk(mText4,black);
            Chunk blueText5 = new Chunk(mText5,black);
            Chunk blueText6 = new Chunk(mText6,black);
            if(mText6.toLowerCase().equals("yes"))
            {
                blueText6 = new Chunk(mText6,green);
            }else{
                blueText6 = new Chunk(mText6,red);
            }
            Chunk blueText7 = new Chunk(mText7,black);
            Chunk blueText8 = new Chunk(mText8,black);
            Chunk blueText9 = new Chunk(mText9,black);
            Chunk blueText10 = new Chunk(mText10,black);
            Chunk blueText11 = new Chunk(mText11,black);
            Chunk blueText12 = new Chunk(mText12,black);
            Chunk blueText13 = new Chunk(mText13,black);



            Chunk caseid = new Chunk("CASE ID:",blue);
            Chunk reportedby = new Chunk("REPORTED BY:",blue);
            Chunk casetype = new Chunk("CASE TYPE:",blue);
            Chunk casetime = new Chunk("CASE OCCURENCE TIME:",blue);
            Chunk reportdetails = new Chunk("REPORT DETAILS:",blue);
            Chunk caseassigned = new Chunk("CASE ASSIGNED:",blue);
            Chunk caseemg = new Chunk("CASE URGENT REPORTS",blue);
            Chunk contectmob = new Chunk("REPORTER PHONE NUMBER",blue);
            Chunk updemail = new Chunk("REPORTER EMAIL ID",blue);
            Chunk updcode = new Chunk("Species",blue);
            Chunk attach = new Chunk("ATTACHMENTS",blue);
            Chunk rep_sign = new Chunk("Reporter's Sign",blue);
            Chunk geolong = new Chunk("Geo-Longitude",blue);
            Chunk geolat = new Chunk("Geo-Latitude",blue);

            Bitmap bitmap = ((BitmapDrawable)case_image.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , stream);
            Image myImg = Image.getInstance(stream.toByteArray());
            myImg.setAlignment(Image.MIDDLE);
            mDoc.add(myImg);

            mDoc.add(new Paragraph(caseid));
            mDoc.add(new Paragraph(blueText1));

            mDoc.add(new Paragraph(reportedby));
            mDoc.add(new Paragraph(blueText2));

            mDoc.add(new Paragraph(casetype));
            mDoc.add(new Paragraph(blueText3));

            mDoc.add(new Paragraph(casetime));
            mDoc.add(new Paragraph(blueText4));

            mDoc.add(new Paragraph(reportdetails));
            mDoc.add(new Paragraph(blueText5));

            mDoc.add(new Paragraph(caseassigned));
            mDoc.add(new Paragraph(blueText6));

            mDoc.add(new Paragraph(caseemg));
            mDoc.add(new Paragraph(blueText7));

            mDoc.add(new Paragraph(contectmob));
            mDoc.add(new Paragraph(blueText8));

            mDoc.add(new Paragraph(updemail));
            mDoc.add(new Paragraph(blueText9));

            mDoc.add(new Paragraph(updcode));
            mDoc.add(new Paragraph(blueText10));

            mDoc.add(new Paragraph(attach));
            mDoc.add(new Paragraph(blueText11));

            mDoc.add(new Paragraph(geolat));
            mDoc.add(new Paragraph(blueText12));

            mDoc.add(new Paragraph(geolong));
            mDoc.add(new Paragraph(blueText13));

           /* mDoc.add(new Paragraph(rep_sign));

            Bitmap bitmap1 = ((BitmapDrawable)uploader_sign.getDrawable()).getBitmap();
            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100 , stream1);
            Image myImg1 = Image.getInstance(stream1.toByteArray());
            myImg1.setAlignment(Image.MIDDLE);
            mDoc.add(myImg1);*/


            mDoc.close();

            Toast.makeText(FullCaseViewActivity.this, mFileName+".pdf\n is saved to\n"+mFilePath, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){

            Toast.makeText(FullCaseViewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
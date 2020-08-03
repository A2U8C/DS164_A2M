package com.abc.sih;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AdminQrCodeGenerateActivity extends AppCompatActivity {

    Button generate;
    EditText div,range,beat,checkpoint_num;
    private ImageView qr_code;
    Button pdf_gen;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_qr_code_generate);

        generate = (Button) findViewById(R.id.btn);
        div = (EditText) findViewById(R.id.division_name);
        range = (EditText) findViewById(R.id.range_name);
        beat = (EditText) findViewById(R.id.beat_name);
        checkpoint_num = (EditText) findViewById(R.id.code_name);
        qr_code = (ImageView) findViewById(R.id.qrcode);
        pdf_gen = (Button) findViewById(R.id.btn1);

        pdf_gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // generatepdf();
            }
        });

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt1 = div.getText().toString();
                String txt2 = range.getText().toString();
                String txt3 = beat.getText().toString();
                String txt4 = checkpoint_num.getText().toString();
                if(txt1!=null && !txt1.isEmpty() && txt2!=null && !txt2.isEmpty() && txt3!=null && !txt3.isEmpty()
                        && txt4!=null && !txt4.isEmpty()){

                    try
                    {
                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                        BitMatrix bitMatrix = multiFormatWriter.encode(txt1+"_"+txt2+"_"+txt3+"_"+txt4, BarcodeFormat.QR_CODE,500,500);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        qr_code.setImageBitmap(bitmap);
                        generatepdf(bitmap,txt1,txt2,txt3,txt4);

                    }
                    catch (WriterException e)
                    {

                        e.printStackTrace();
                    }

                }
            }
        });




    }
    public void generatepdf(Bitmap bitmap, String txt1,String txt2, String txt3, String txt4){

        Document mDoc = new Document();

        String mFileName = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis());

        String mFilePath = Environment.getExternalStorageDirectory()+ File.separator+"SIHCaseDir" + "/" + mFileName + ".pdf";
        try{
            PdfWriter.getInstance(mDoc,new FileOutputStream(mFilePath));

            mDoc.open();

            Font blue = new Font(Font.FontFamily.HELVETICA, 16, Font.NORMAL, BaseColor.BLUE);
            Font black = new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL, BaseColor.BLACK);

            // Change image's name and extension.
            ///Image image =  Bitmap.decode
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , stream);
            Image myImg = Image.getInstance(stream.toByteArray());
            myImg.setAlignment(Image.MIDDLE);
            mDoc.add(myImg);

            Chunk blueText1 = new Chunk(txt1, blue);
            Chunk blueText2 = new Chunk(txt2,blue);
            Chunk blueText3 = new Chunk(txt3, blue);
            Chunk blueText4 = new Chunk(txt4,blue);

            Chunk head1 = new Chunk("Division Name:",black);
            Chunk head2 = new Chunk("Range Name:",black);
            Chunk head3 = new Chunk("Beat Name:",black);
            Chunk head4 = new Chunk("Code Number:",black);


            mDoc.add(new Paragraph(head1));
            mDoc.add(new Paragraph(blueText1));

            mDoc.add(new Paragraph(head2));
            mDoc.add(new Paragraph(blueText2));

            mDoc.add(new Paragraph(head3));
            mDoc.add(new Paragraph(blueText3));

            mDoc.add(new Paragraph(head4));
            mDoc.add(new Paragraph(blueText4));


            // float scaler = ((mDoc.getPageSize().getWidth() - mDoc.leftMargin()
            //         - mDoc.rightMargin() - 0) / image.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
            // image.scalePercent(scaler);
            //  image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);

            // mDoc.add(image);
            mDoc.close();

            Toast.makeText(AdminQrCodeGenerateActivity.this, mFileName+".pdf\n is saved to\n"+mFilePath, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(AdminQrCodeGenerateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }



}
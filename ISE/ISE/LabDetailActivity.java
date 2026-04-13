package com.example.ise;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LabDetailActivity extends AppCompatActivity {

    ImageView image;
    TextView text, title;
    Button btnBack;

    int[] images = {
            R.drawable.lab1,R.drawable.lab2,R.drawable.lab3,
            R.drawable.lab4,R.drawable.lab5,R.drawable.lab6,
            R.drawable.lab7,R.drawable.lab8,R.drawable.lab9,
            R.drawable.lab10,R.drawable.lab11,R.drawable.lab12
    };

    String[] labNames = {
            "Java Programming Lab", "Advanced Database System Lab", "Database Engineering Lab",
            "R Programming Lab", "Project Lab", "Research Lab",
            "Web Technology Lab", "Python Programming Lab", "C Programming Lab",
            "System Programming Lab", "Mobile Application Development Lab", "C++ Programming Lab"
    };

    String[] desc = {
            "The JAVA Programming lab is used to perform practicals of Advance C Programming, Data Structures, Information Security, Domain specific mini project and Compiler Construction. The software's that are installed includes Ubuntu OS, Open JDK and GCC packages.\n\nKey Features & Facilities:\n• Dell OptiPlex 3020 Machines\n• Microprocessor Anshuman Kit\n• IOT Kit\n• HP Laser Jet 1020 Printer\n• 6 KVA Rudra UPS\n• Courses: Java Programming, JavaScript, Web Development",
            "The Advance Database System Programming lab is used to perform practicals of Project I, Project II, Advance C Programming, System Programming and Database Engineering. The software's that are installed includes Ubuntu OS, MySQL, Atom editor, Brackets editor, GCC, g++ and Open JDK packages.\n\nKey Features & Facilities:\n• Dell OptiPlex 5090 & 390 Machines\n• 6 KVA Renutron UPS\n• HP Laser Jet P1008 Printer",
            "The Database Engineering lab is used to perform practicals of Database Engineering, Web Technology, Java Programming, Domain specific mini project and Computer Network Laboratory. The software's that are installed includes Ubuntu OS, MySQL, MongoDB, GCC, g++ and Open JDK packages.\n\nKey Features & Facilities:\n• Dell OptiPlex 3020 Machines\n• 6 KVA Rudra UPS",
            "The R Programming lab is used to perform practicals of Data Structures, Web Technology, Java Programming and CNN. The software's that are installed includes Ubuntu OS, Rstudio, GCC, g++ and Open JDK packages.\n\nKey Features & Facilities:\n• Dell OptiPlex 5090 Machines\n• 6 KVA Renutron UPS",
            "The Project lab is used to perform practicals of Project I, Project II, Advance C Programming, System Programming and Database Engineering. The software's that are installed includes Ubuntu OS, MySQL, Atom editor, Brackets editor, GCC, g++ and OpenJDK packages.\n\nKey Features & Facilities:\n• Dell OptiPlex 5090 & 390 Machines\n• 6 KVA Renutron UPS\n• HP Laser Jet P1008 Printer",
            "The Research Lab is used to perform practicals of Project I/II, Data Structures, Java Programming, Operating System and Domain specific mini project. The software's that are installed includes Ubuntu OS, Atom editor, Brackets editor, PyCharm editor, MySQL, MongoDB, GCC, and Open JDK packages.\n\nKey Features & Facilities:\n• Dell OptiPlex 390 Machines\n• 6 KVA Emerson UPS\n• HP 1020 LaserJet Printer",
            "The Web Technology lab is used to perform practicals of Web Technology, Advance C Programming, Java Programming, CNN and Domain specific mini project. The software's that are installed includes Ubuntu and Window OS, XAMPP server, Wampserver, Atom editor, Brackets editor, GCC, and Open JDK packages.\n\nKey Features & Facilities:\n• Dell OptiPlex 5090 Machines\n• HP Laser Jet M1522 Printer\n• 6 KVA Renutron UPS",
            "The Python Programming lab is used to perform practicals of Compiler Construction, BDA, Operating System, Database Engineering and Compiler Construction. The software's that are installed includes Ubuntu OS, PyCharm editor, Atom editor, Brackets editor, GCC, g++ and Open JDK packages.\n\nKey Features & Facilities:\n• Dell OptiPlex 3020 Machines\n• 6 KVA Renutron UPS\n• Canon Image Class LBC6030w Printer",
            "The C Programming lab is used to perform practicals of C language. The software's that are installed includes Ubuntu OS, GCC, g++ .\n\nKey Features & Facilities:\n• HP 280 GT Machines\n• 6 KVA Rudra UPS",
            "Laboratory for system programming and low-level software development.\n\nKey Features & Facilities:\n• Dell OptiPlex 7000 i7 Machines\n• Courses: Database Engineering, Advanced Database Systems",
            "The Mobile Application Development lab is used to perform practical's of Mobile Application development, Data Structures, Java Programming, Information Security and Domain specific mini project. The software's that are installed includes Ubuntu and Window OS, Android studio, Atom editor, Brackets editor, GCC, and Open JDK packages.\n\nKey Features & Facilities:\n• Dell OptiPlex 5090 Machines\n• 6 KVA Rudra UPS\n• HP Laser Jet P2055dn printer",
            "The C++ Programming lab is used to perform practicals of C++ language. The software's that are installed includes Ubuntu OS, GCC, g++.\n\nKey Features & Facilities:\n• Dell OptiPlex 3020 Machines\n• 6 KVA Renutron UPS."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_detail);

        // Hide ActionBar if it exists (maintaining NoActionBar consistency)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        image = findViewById(R.id.detailImage);
        text = findViewById(R.id.detailText);
        title = findViewById(R.id.labTitle);
        btnBack = findViewById(R.id.btnBackDetail);

        int position = getIntent().getIntExtra("lab", 0);

        title.setText(labNames[position]);
        image.setImageResource(images[position]);
        text.setText(desc[position]);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
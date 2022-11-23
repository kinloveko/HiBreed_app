package com.example.hi_breed.userFile.shop.pet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.hi_breed.R;
import com.example.hi_breed.adapter.petClass;
import com.example.hi_breed.adapter.petImagesRecyclerAdapter;
import com.example.hi_breed.userFile.home.user_home_fragment;
import com.example.hi_breed.userFile.profile.user_profile_edit;
import com.example.hi_breed.userFile.shop.user_shop_fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class pet_add extends AppCompatActivity implements petImagesRecyclerAdapter.CountOfImagesWhenRemovedPet,  petImagesRecyclerAdapter.itemClickListenerPet {
    String[] breed = {"Afador", "Afaird", "Affen Border Terrier", "Affen Spaniel", "Affen Tzu", "Affenchon", "Affengriffon", "Affenhuahua",
            "Affenpinscher", "Affenpoo", "Affenpug", "Affenshire", "Affenwich", "Afghan Chon", "Afghan Collie", "Afghan Hound",
            "Afghan Retriever", "Afghan Sheepdog", "Afghan Spaniel", "Afollie", "Airedale Shepherd", "Airedale Terrier", "Airedoodle",
            "Akbash", "Akbash Pyrenees", "Akbash Rottie", "Aki-poo", "Akita", "Akita Basset", "Akita Bernard", "Akita Chow", "Akita Pit",
            "Akita Shepherd", "Akitamatian", "Alaskan Goldenmute", "Alaskan Husky Shepherd", "Alaskan Irish Setsky", "Alaskan Klee Kai",
            "Alaskan Malador", "Alaskan Malamute", "Alaskan Pit Bull Terrier", "Alaskan Shepherd", "Alaskan Weimsky", "Alusky",
            "American Bandogge", "American Boston Bull Terrier", "American Boxer Foxhound", "American Bull-Aussie", "American Bull Dane",
            "American Bull Dogue De Bordeaux", "American Bull-Jack", "American Bull Pei", "American Bull Staffy", "American Bulldog",
            "American Bulldog Shepherd", "American Bullhuahua", "American Bullweiler", "American Chow Bulldog", "American Eagle Dog",
            "American English Coonhound",
            "American Eskimo Dog", "American Foxeagle", "American Foxhound", "American Foxy Dane", "American French Bull Terrier",
            "American Hairless Terrier", "American Lab Foxhound", "American Leopard Hound", "American Pit Bull Terrier",
            "American Pugabull", "American Staffordshire Terrier", "American Water Spaniel", "Amstiff", "Anatolian Shepherd Dog",
            "Appenzeller Sennenhunde", "Auggie", "Aussie Rottie", "Aussie Shiba", "Aussiedoodle", "Aussiepom", "Australian Boxherd",
            "Australian Cattle Dog", "Australian Kelpie", "Australian Retriever", "Australian Shepherd", "Australian Shepherd Husky",
            "Australian Shepherd Lab Mix", "Australian Shepherd Pit Bull Mix", "Australian Stumpy Tail Cattle Dog", "Australian Terrier",
            "Azawakh", "B", "Ba-Shar", "Bagle Hound", "Barbado da Terceira", "Barbet", "Bascottie", "Basenji", "Bassador", "Basschshund",
            "Basset Bleu de Gascogne", "Basset Fauve de Bretagne", "Basset Hound", "Basset Jack", "Basset Retriever", "Bassetoodle",
            "Bavarian Mountain Scent Hound", "Beabull", "Beagle", "Beaglier", "Bearded Collie", "Bedlington Terrier", "Belgian Laekenois",
            "Belgian Malinois", "Belgian Sheepdog", "Belgian Tervuren", "Bergamasco Sheepdog", "Berger Picard", "Bernedoodle",
            "Bernese Mountain Dog", "Bichon Frise", "Biewer Terrier", "Black and Tan Coonhound", "Black Mouth Cur", "Black Russian Terrier",
            "Bloodhound", "Blue Lacy", "Bluetick Coonhound", "Bocker", "Boerboel", "Boglen Terrier", "Bohemian Shepherd", "Bolognese",
            "Borador", "Border Collie", "Border Sheepdog", "Border Terrier", "Bordoodle", "Borzoi", "BoShih", "Bossie", "Boston Boxer",
            "Boston Terrier", "Boston Terrier Pekingese Mix", "Bouvier des Flandres", "Boxador", "Boxer", "Boxerdoodle", "Boxmatian",
            "Boxweiler", "Boykin Spaniel", "Bracco Italiano", "Braque du Bourbonnais", "Braque Francais Pyrenean", "Briard", "Brittany",
            "Broholmer", "Brussels Griffon", "Bugg", "Bull Arab", "Bull-Pei", "Bull Terrier", "Bullador", "Bullboxer Pit", "Bulldog",
            "Bullmastiff", "Bullmatian", "C", "Cairn Terrier", "Canaan Dog", "Cane Corso", "Cardigan Welsh Corgi", "Carolina Dog",
            "Catahoula Bulldog", "Catahoula Leopard Dog", "Caucasian Shepherd Dog", "Cav-a-Jack", "Cavachon", "Cavador",
            "Cavalier King Charles Spaniel", "Cavapoo", "Central Asian Shepherd Dog", "Cesky Terrier", "Chabrador", "Cheagle",
            "Chesapeake Bay Retriever", "Chi Chi", "Chi-Poo", "Chigi", "Chihuahua", "Chilier", "Chinese Crested", "Chinese Shar-Pei",
            "Chinook", "Chion", "Chipin", "Chiweenie", "Chorkie", "Chow Chow", "Chow Shepherd", "Chug", "Chusky", "Cirneco dell’Etna",
            "Clumber Spaniel", "Cockalier", "Cockapoo", "Cocker Spaniel", "Collie", "Corgi Inu", "Corgidor", "Corman Shepherd",
            "Coton de Tulear", "Croatian Sheepdog", "Curly-Coated Retriever", "Czechoslovakian Vlcak", "D", "Dachsador", "Dachshund",
            "Dalmatian", "Dandie Dinmont Terrier", "Daniff", "Danish-Swedish Farmdog", "Deutscher Wachtelhund", "Doberdor",
            "Doberman Pinscher", "Docker", "Dogo Argentino", "Dogue de Bordeaux", "Dorgi", "Dorkie", "Doxiepoo", "Doxle",
            "Drentsche Patrijshond", "Drever", "Dutch Shepherd", "E", "English Cocker Spaniel", "English Foxhound", "English Setter",
            "English Springer Spaniel", "English Toy Spaniel", "Entlebucher Mountain Dog", "Estrela Mountain Dog", "Eurasier",
            "F", "Field Spaniel", "Fila Brasileiro", "Finnish Lapphund", "Finnish Spitz", "Flat-Coated Retriever", "Fox Terrier",
            "French Bulldog", "French Bullhuahua", "French Spaniel", "Frenchton", "Frengle", "G", "German Longhaired Pointer",
            "German Pinscher", "German Shepherd Dog", "German Shepherd Pit Bull", "German Shepherd Rottweiler Mix", "German Sheprador",
            "German Shorthaired Pointer", "German Spitz", "German Wirehaired Pointer", "Giant Schnauzer", "Glen of Imaal Terrier",
            "Goberian", "Goldador", "Golden Cocker Retriever", "Golden Mountain Dog", "Golden Retriever", "Golden Retriever Corgi",
            "Golden Shepherd", "Goldendoodle", "Gollie", "Gordon Setter", "Great Dane", "Great Pyrenees", "Greater Swiss Mountain Dog",
            "Greyador", "Greyhound", "H", "Hamiltonstovare", "Hanoverian Scenthound", "Harrier", "Havanese", "Havapoo", "Hokkaido", "Horgi"
            , "Hovawart", "Huskita", "Huskydoodle", "I", "Ibizan Hound", "Icelandic Sheepdog", "Irish Red And White Setter", "Irish Setter",
            "Irish Terrier", "Irish Water Spaniel", "Irish Wolfhound", "Italian Greyhound", "J", "Jack-A-Poo", "Jack Chi",
            "Jack Russell Terrier", "Jackshund", "Jagdterrier", "Japanese Chin", "Japanese Spitz", "K", "Korean Jindo Dog",
            "Kai Ken", "Kangal Shepherd Dog", "Karelian Bear Dog", "Keeshond", "Kerry Blue Terrier", "King Shepherd", "Kishu Ken",
            "Komondor", "Kooikerhondje", "Kromfohrlander", "Kuvasz", "Kyi-Leo", "L", "Lab Pointer", "Labernese", "Labmaraner",
            "Labrabull", "Labradane", "Labradoodle", "Labrador Retriever", "Labrastaff", "Labsky", "Lagotto Romagnolo", "Lakeland Terrier",
            "Lancashire Heeler", "Lapponian Herder", "Leonberger", "Lhasa Apso", "Lhasapoo", "Lowchen", "M", "Maltese", "Maltese Shih Tzu",
            "Maltipoo", "Manchester Terrier", "Maremma Sheepdog", "Mastador", "Mastiff", "Miniature Pinscher", "Miniature Schnauzer",
            "Morkie", "Mountain Cur", "Mountain Feist", "Mudi", "Mutt (Mixed)", "N", "Neapolitan Mastiff", "Newfoundland", "Norfolk Terrier",
            "Northern Inuit Dog", "Norwegian Buhund", "Norwegian Elkhound", "Norwegian Lundehund", "Norwich Terrier",
            "Nova Scotia Duck Tolling Retriever", "O", "Old English Sheepdog", "Otterhound", "P", "Papillon", "Papipoo",
            "Patterdale Terrier", "Peekapoo", "Pekingese", "Pembroke Welsh Corgi", "Perro de Presa Canario", "Petit Basset Griffon Vendéen",
            "Pharaoh Hound", "Pitsky", "Plott", "Pocket Beagle", "Pointer", "Polish Lowland Sheepdog", "Pomapoo", "Pomchi", "Pomeagle",
            "Pomeranian", "Pomsky", "Poochon", "Poodle", "Porcelaine", "Portuguese Podengo Pequeno", "Portuguese Pointer",
            "Portuguese Sheepdog", "Portuguese Water Dog", "Pudelpointer", "Pug", "Pugalier", "Puggle", "Puginese", "Puli",
            "Pyredoodle", "Pyrenean Mastiff", "Pyrenean Shepherd", "R", "Rafeiro do Alentejo", "Rat Terrier", "Redbone Coonhound",
            "Rhodesian Ridgeback", "Romanian Mioritic Shepherd Dog", "Rottador", "Rottle", "Rottweiler", "Russian Toy",
            "Russian Tsvetnaya Bolonka", "S", "Saint Berdoodle", "Saint Bernard", "Saluki", "Samoyed", "Samusky", "Schapendoes",
            "Schipperke", "Schnocker", "Schnoodle", "Scottish Deerhound", "Scottish Terrier", "Sealyham Terrier", "Segugio Italiano",
            "Sheepadoodle", "Shepsky", "Shetland Sheepdog", "Shiba Inu", "Shichon", "Shih-Poo", "Shih Tzu", "Shikoku", "Shiloh Shepherd",
            "Shiranian", "Shollie", "Shorkie", "Siberian Husky", "Silken Windhound", "Silky Terrier", "Skye Terrier", "Sloughi",
            "Slovakian Wirehaired Pointer", "Slovensky Cuvac", "Slovensky Kopov", "Small Munsterlander Pointer",
            "Soft Coated Wheaten Terrier", "Spanish Mastiff", "Spanish Water Dog", "Spinone Italiano", "Springador", "Stabyhoun",
            "Staffordshire Bull Terrier", "Staffy Bull Bullmastiff", "Standard Schnauzer", "Sussex Spaniel", "Swedish Lapphund",
            "Swedish Vallhund", "T", "Taiwan Dog", "Terripoo", "Texas Heeler", "Thai Ridgeback", "Tibetan Mastiff", "Tibetan Spaniel",
            "Tibetan Terrier", "Tornjak", "Tosa", "Toy Fox Terrier", "Transylvanian Hound", "Treeing Tennessee Brindle",
            "Treeing Walker Coonhound", "V", "Valley Bulldog", "Vizsla", "W", "Weimaraner", "Welsh Springer Spaniel", "Welsh Terrier",
            "West Highland White Terrier", "Westiepoo", "Whippet", "Whoodle", "Wirehaired Pointing Griffon", "Working Kelpie", "X",
            "Xoloitzcuintli", "Y", "Yakutian Laika", "Yorkipoo", "Yorkshire Terrier"};

    ListView listView;
    EditText editText;
    RelativeLayout addCustom;
    ArrayAdapter<String> arrayAdapter;
    RelativeLayout vaccine_container;
    LinearLayout added_vaccine;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    //ImageView
    ImageView petNameClear,petDescClear,petColorClear;
    RecyclerView pet_images_view;
    petImagesRecyclerAdapter pet_adapter;
    List<String> saveVaccine = new ArrayList<>();
    ArrayList<Uri> uri = new ArrayList<>();
    Uri imageUri;
    private int countOfImages;
    private final String selectGender = "male";
    String numberHolder;
    TextView genderTextView,petBreedTextView,petBirthdayTextView,petPriceTextView,petNameCount,petDescCount,petColorCount,dewormingTextView,dewormCountOfTimes,text3,text4;

    //Relative Layout
    RelativeLayout genderLayout,categoryLayout,birthdayLayout,priceLayout,vaccineLayout;
    //Linear Layout
    LinearLayout backLayoutPet;
    CardView petPhotoCardView;
    TextInputEditText petNameEdit,petDescEdit,petColorEdit;
    EditText textIn;
    TextView imageClear;
    //Button
    Button buttonAdd,pet_add_create_Button;

   ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_add);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffc36e"));
        progressDialog = new ProgressDialog(this);

        //User
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //Firebase
         firebaseDatabase = FirebaseDatabase.getInstance();
         databaseReference = firebaseDatabase.getReference("Breeder").child(firebaseUser.getUid()).child("Breeder Shop");

         firebaseStorage = FirebaseStorage.getInstance();

         //ImageView
        petDescClear =findViewById(R.id.petDescClearButton);
        petNameClear = findViewById(R.id.petNameclearButton);
        petColorClear = findViewById(R.id.petColorClearButton);
         //EditText
        petNameEdit = findViewById(R.id.petNameEdit);
        petNameCount = findViewById(R.id.petNamecountID);
        petDescEdit = findViewById(R.id.petDescEdit);
        petDescCount = findViewById(R.id.petDescCountID);
        petColorEdit = findViewById(R.id.petColorEdit);
        petColorCount = findViewById(R.id.petColorCount);
        //Button
        pet_add_create_Button = findViewById(R.id.pet_add_create_Button);
        //RecyclerView
        pet_images_view = findViewById(R.id.pet_images_view);
        //adapter
        pet_adapter = new petImagesRecyclerAdapter(uri,getApplicationContext(),this, this);
        pet_images_view.setLayoutManager(new GridLayoutManager(this,4));
        pet_images_view.setAdapter(pet_adapter);
        //LinearLayout
        backLayoutPet = findViewById(R.id.backLayoutPet);
        //TextView
        dewormingTextView = findViewById(R.id.dewormingTextView);
        dewormCountOfTimes = findViewById(R.id.dewormCountOfTimes);
        petPriceTextView = findViewById(R.id.petPriceTextView);
        petBirthdayTextView = findViewById(R.id.petBirthdayTextView);
        genderTextView = findViewById(R.id.petGender);
        petBreedTextView = findViewById(R.id.petBreed);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        //CardView
        petPhotoCardView = findViewById(R.id.petPhotoCardView);

        textIn =  findViewById(R.id.textin);
        imageClear = findViewById(R.id.clearIDText);
        textIn.addTextChangedListener(addVaccineTextWatcher);
        buttonAdd = findViewById(R.id.add);

        //RelativeLayout
        vaccineLayout = findViewById(R.id.vaccineLayout);
        priceLayout = findViewById(R.id.priceLayout);
        birthdayLayout = findViewById(R.id.birthdayLayout);
        categoryLayout = findViewById(R.id.categoryLayout);
        vaccine_container = findViewById(R.id.save_container);
        added_vaccine = findViewById(R.id.added_vaccine);

        backLayoutPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pet_add.this.onBackPressed();
            }
        });
        categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryDialog();
            }
        });
        vaccineLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vaccine_container.isShown()){
                    vaccine_container.setVisibility(View.GONE);
                }
                else{
                   openVaccineDropdown();
                    vaccine_container.setVisibility(View.VISIBLE);
                }
            }
        });
        genderLayout = findViewById(R.id.genderLayout);
        genderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showGenderDialog();
            }
        });

        birthdayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
  /*              datePickerClass pickerClass = new datePickerClass(datePickerDialog,pet_add.this,R.id.petBirthdayTextView);
               if(pickerClass!=null) {
                   pickerClass.initDatePicker();
               }
            */   openBirthdayDialog();

            }
        });
        priceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPrice();
            }
        });
        petPhotoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePermission();
            }
        });
        pet_add_create_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadToFirebaseDatabase();
            }
        });

        if(petNameEdit.getText() !=null || petNameEdit.getText().equals("")) {
            petNameEdit.addTextChangedListener(petNameTextEditorWatcher);
        }
        if(petDescEdit.getText() !=null || petDescEdit.getText().equals("")) {
            petDescEdit.addTextChangedListener(petDescTextEditorWatcher);
        }
        if( petColorEdit.getText() !=null ||  petColorEdit.getText().equals("")) {
            petColorEdit.addTextChangedListener(petColorTextEditorWatcher);
        }
    }

    private void openBirthdayDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(pet_add.this);
            View view = View.inflate(pet_add.this,R.layout.pet_add_birthday_dialog,null);
            DatePicker datePicker = view.findViewById(R.id.datePicker);
            datePicker.setMaxDate(System.currentTimeMillis());
            builder.setView(view);
            MaterialButton cancel,save;

            cancel = view.findViewById(R.id.birthday_dialog_btn_cancel);
            save = view.findViewById(R.id.birthday_dialog_btn_done);
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            save.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    int   day  = datePicker.getDayOfMonth();
                    int   month= datePicker.getMonth();
                    int   year = datePicker.getYear();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);

                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    String formatedDate = sdf.format(calendar.getTime());
                    petBirthdayTextView.setText(formatedDate);
                    Toast.makeText(pet_add.this, formatedDate, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
    }

    private int count = 0;    String numberHolders;
    private static int counter = 0;
    View saveView;
    private void openVaccineDropdown() {

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View arg0) {
                if (textIn.getText().toString().equals("") || textIn.getText().toString() == null) {
                    Toast.makeText(pet_add.this, "Input is empty", Toast.LENGTH_SHORT).show();
                } else {

                    AlertDialog.Builder numberBuilder = new AlertDialog.Builder(pet_add.this);
                    View view = View.inflate(pet_add.this, R.layout.pet_add_vaccine_taken_dialog, null);
                    numberBuilder.setCancelable(false);
                    MaterialButton done,cancel;
                    done = view.findViewById(R.id.btn_done);
                    cancel = view.findViewById(R.id.btn_cancel);
                    NumberPicker numberPicker = view.findViewById(R.id.numberPicker);
                    numberPicker.setMinValue(1);
                    numberPicker.setMaxValue(5);
                    numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                            numberHolder = String.valueOf(newVal);

                        }
                    });

                    numberBuilder.setView(view);
                   AlertDialog finalAlert = numberBuilder.create();
                    finalAlert.show();
                    finalAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            count +=1;
                            LayoutInflater layoutInflater =
                                    (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            saveView = layoutInflater.inflate(R.layout.pet_add_vaccine_save_row, null);
                            TextView save_textOut = saveView.findViewById(R.id.vaccine_save_textout);
                            TextView save_count = saveView.findViewById(R.id.vaccine_save_count);

                            if(numberHolder == ""|| numberHolder == null || numberHolder == "0"){
                                numberHolder = "1";
                            }

                            save_textOut.setText(textIn.getText().toString());
                            save_count.setText(numberHolder);


                            save_textOut.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(pet_add.this);
                                    View views = View.inflate(pet_add.this, R.layout.pet_add_edit_vaccine_dialog, null);

                                    EditText edit =  views.findViewById(R.id.edit_vaccine_dialog_textin);
                                    MaterialButton done,cancel;
                                    done = views.findViewById(R.id.edit_vaccine_dialog_btn_done);
                                    cancel = views.findViewById(R.id.edit_vaccine_dialog_btn_cancel);

                                    builder.setView(views);
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                    alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                                    done.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if(edit.getText().toString() == null || edit.getText().toString().equals("") ){
                                                counter = 0;

                                            }else {
                                                counter = 1;
                                                AlertDialog.Builder numberBuilder = new AlertDialog.Builder(pet_add.this);
                                                View view = View.inflate(pet_add.this, R.layout.pet_add_vaccine_taken_dialog, null);
                                                MaterialButton done_copy,cancel_copy;
                                                done_copy = view.findViewById(R.id.btn_done);
                                                cancel_copy = view.findViewById(R.id.btn_cancel);
                                                NumberPicker numberPicker = view.findViewById(R.id.numberPicker);
                                                numberPicker.setMinValue(1);
                                                numberPicker.setMaxValue(5);
                                                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                                    @Override
                                                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                                        numberHolders = String.valueOf(newVal);
                                                    }
                                                });
                                                numberBuilder.setView(view);
                                                AlertDialog finalAlert = numberBuilder.create();
                                                finalAlert.show();
                                                finalAlert.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dropshadow));
                                                done_copy.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        ((LinearLayout) saveView.getParent()).removeView(saveView);
                                                        saveVaccine.remove(saveView);
                                                        save_textOut.setText(edit.getText().toString());
                                                        if(numberHolders == ""|| numberHolders == null || numberHolders == "0") {
                                                            numberHolders = "1";
                                                        }
                                                        save_count.setText(numberHolders);
                                                        added_vaccine.addView(saveView);
                                                        counter = 1;
                                                        finalAlert.dismiss();
                                                    }
                                                });
                                                cancel_copy.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        finalAlert.dismiss();
                                                    }
                                                });
                                                numberHolders="";
                                            }
                                            if(counter==0){
                                                Toast.makeText(pet_add.this, "Cannot make any changes", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                alert.dismiss();
                                            }
                                        }
                                    });
                                    cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                        alert.dismiss();
                                        }
                                    });

                                }
                            });
                            Button removeButton = saveView.findViewById(R.id.vaccine_save_remove);
                            removeButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ((LinearLayout) saveView.getParent()).removeView(saveView);
                                    count-=1;
                                    if(count==0){
                                        text3.setVisibility(View.GONE);
                                        text4.setVisibility(View.GONE);
                                    }
                                    else{
                                        Toast.makeText(pet_add.this, "Cannot make any changes", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            if(count!=0){
                                text3.setVisibility(View.VISIBLE);
                                text4.setVisibility(View.VISIBLE);
                            }
                            textIn.getText().clear();

                            added_vaccine.addView(saveView);



                            finalAlert.dismiss();
                            numberHolder="";

                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finalAlert.dismiss();
                        }
                    });
                }
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showCategoryDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //ListView

        View pop = View.inflate(pet_add.this,R.layout.pet_add_search_dialog,null);
        addCustom = pop.findViewById(R.id.search_customBreedID);
        editText = pop.findViewById(R.id.search_searchEditID);
        listView = pop.findViewById(R.id.search_breedListView);
        MaterialButton cancel;
        cancel = pop.findViewById(R.id.search_dialog_btn_cancel);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,breed);
        listView.setAdapter(arrayAdapter);
        builder.setView(pop);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if(editText.getText()!=null || editText.getText().equals("")) {
            editText.addTextChangedListener(filterTextWatcher);
        }

        if(listView.getCount() == 0 || listView == null){
                listView.setVisibility(View.GONE);
                listView.setCacheColorHint(Color.TRANSPARENT);
                listView.setBackground(new ColorDrawable(Color.TRANSPARENT));
                listView.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        }
        else{
            listView.setBackground(getDrawable(R.drawable.shape));
            listView.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            listView.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = 400;
            listView.setLayoutParams(params);
            listView.requestLayout();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String holder = listView.getItemAtPosition(position).toString();
                petBreedTextView.setText(holder);
                Toast.makeText(pet_add.this, holder, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        addCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(pet_add.this);
                View custom = View.inflate(pet_add.this,R.layout.pet_add_custom_breed_dialog,null);
                EditText customEdit;
                MaterialButton cancel,done;
                customEdit = custom.findViewById(R.id.custom_breed_dialog_textin);
                cancel = custom.findViewById(R.id.custom_breed_dialog_btn_cancel);
                done = custom.findViewById(R.id.custom_breed_dialog_btn_done);

                  builder.setView(custom);
                  AlertDialog dialogAddCustom = builder.create();
                  dialogAddCustom.show();
                  dialogAddCustom.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                     done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(customEdit.getText().toString() == null || customEdit.getText().toString().equals("")){
                            counter = 0;
                        }
                        else{
                            petBreedTextView.setText(customEdit.getText().toString());
                            counter=1;
                        }
                        if (counter!=0){
                            Toast.makeText(pet_add.this, customEdit.getText().toString(), Toast.LENGTH_SHORT).show();
                            dialogAddCustom.dismiss();
                        }
                        else{
                            Toast.makeText(pet_add.this, "Cannot make any changes", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogAddCustom.dismiss();
                    }
                });

            }
        });
    }
    @SuppressLint({"UseCompatLoadingForDrawables", "ResourceType"})
    private void showDialogPrice() {

        AlertDialog.Builder builder = new AlertDialog.Builder(pet_add.this);
       View view = View.inflate(pet_add.this,R.layout.pet_add_vaccine_price_dialog,null);
       MaterialButton done, cancel;
       EditText text;
       text = view.findViewById(R.id.price_dialog_textin);
       done = view.findViewById(R.id.price_dialog_btn_done);
       cancel = view.findViewById(R.id.price_dialog_btn_cancel);



        builder.setView(view);
        AlertDialog alert = builder.create();
        alert.show();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.getText().toString() == null || text.getText().toString().equals("")) {
                    counter = 0;

                } else {
                    petPriceTextView.setText(text.getText().toString());
                    Toast.makeText(pet_add.this, text.getText().toString(), Toast.LENGTH_SHORT).show();
                    counter = 1;
                }
                if(counter !=0){
                    alert.dismiss();
                }
                else{
                    Toast.makeText(pet_add.this, "Cannot make any changes", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               alert.dismiss();
            }
        });


    }
    String genderHolder="";
    private void showGenderDialog() {
        String[] gender = {"Male","Female"};
        AlertDialog.Builder builder =new AlertDialog.Builder(pet_add.this);
        View view = View.inflate(pet_add.this,R.layout.pet_add_gender_dialog,null);
        MaterialButton done,cancel;
        done = view.findViewById(R.id.gender_dialog_btn_done);
        cancel = view.findViewById(R.id.gender_dialog_btn_cancel);
        
        NumberPicker picker = view.findViewById(R.id.gender_numberPicker);
        picker.setMinValue(0);
        picker.setMaxValue(gender.length -1 );
        picker.setDisplayedValues(gender);
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        picker.setWrapSelectorWheel(false);
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                genderHolder = String.valueOf(newVal);
            }
        });
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(genderHolder == ""|| genderHolder == null){
                    genderHolder = "0";
                }
                genderTextView.setText(gender[Integer.parseInt(genderHolder)]);
                Toast.makeText(pet_add.this, gender[Integer.parseInt(genderHolder)], Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
                genderHolder="";
    }

    @SuppressLint("ObsoleteSdkInt")
    private void imagePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        }
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),1);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && null != data){

            //this part is for to get multiple images
            if(data.getClipData() != null){

                countOfImages = data.getClipData().getItemCount();
                for(int j = 0; j<countOfImages; j++){

                    if(uri.size() < 5){
                        imageUri = data.getClipData().getItemAt(j).getUri();
                        pet_images_view.setVisibility(View.VISIBLE);
                        uri.add(imageUri);
                    }else{
                        Toast.makeText(this,"Not allowed to pick more than 5 images",Toast.LENGTH_SHORT).show();
                    }
                }
                //then notify the adapter
                pet_adapter.notifyDataSetChanged();
                Toast.makeText(this,uri.size()+": selected",Toast.LENGTH_SHORT).show();
            }else{
                //this is for single images
                if(uri.size() <5){
                    imageUri =data.getData();
                    uri.add(imageUri);
                    pet_images_view.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(this,"Not allowed to pick more than 5 images",Toast.LENGTH_SHORT).show();
                }
            }
            pet_adapter.notifyDataSetChanged();
            Toast.makeText(this,uri.size()+": selected",Toast.LENGTH_SHORT).show();
        }
        else{
            //this code is for if user not picked any image
            pet_images_view.setVisibility(View.GONE);
            Toast.makeText(this,"You Haven't Pick any image",Toast.LENGTH_SHORT).show();
        }
    }
    
    
    private void compressImages(){

        for (int i = 0; i< uri.size(); i++){
            try{
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri.get(i));
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG,60,stream);
                byte[] imageByte = stream.toByteArray();
     /*         uploadToFirebaseStorage(imageByte);*/
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    private int upload = 0;
   
    private void uploadToFirebaseStorage(int countIDD) {
        String userID="";
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            userID = firebaseUser.getUid();
        }

        StorageReference ImageFolder = FirebaseStorage.getInstance().getReference("Breeder pictures/"+userID+"/"+"Pet Images/"+"Pet"+countIDD);
        for(upload = 0;upload<uri.size();upload++){
                Uri Image = uri.get(upload);
                StorageReference imageName = ImageFolder.child(Image.getLastPathSegment());
                imageName.putFile(Image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uris) {
                                String url = String.valueOf(uris);
                                StringLink(url,countIDD);

                            }
                        });
                    }
                });
            }
        }
    private static int load = 0;
    private void StringLink(String url,int countIDD) {
        List<String> hashMap = new ArrayList<String>();
        hashMap.add(url);

        FirebaseDatabase.getInstance().getReference().child("Breeder").child(firebaseUser.getUid()).child("Breeder Shop").child("Pet").child("Pet"+countIDD).child("pet_images").push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
        firebaseDatabase.getReference("Shop").child(firebaseUser.getUid()).child("Pet").child("Pet" + countIDD).child("pet_images").push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
        firebaseDatabase.getReference("Pet").child("Pet"+countIDD).child("pet_images").push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

    }



    private void transistion(){
        Thread mSplashThread = new Thread() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(pet_add.this,user_home_fragment.class);
                pet_add.this.overridePendingTransition(R.drawable.fade_in, R.drawable.fade_out);
                startActivity(intent);
                finish();
            }
        };
        mSplashThread.start();
    }



    int countID = 1;
    private void uploadToFirebaseDatabase() {
        String petName = Objects.requireNonNull(petNameEdit.getText()).toString();
        String description = Objects.requireNonNull(petDescEdit.getText()).toString();
        String colorMarkings = Objects.requireNonNull(petColorEdit.getText()).toString();
        String breedCategory = petBreedTextView.getText().toString();
        String genderPet = genderTextView.getText().toString();
        String birthdayPet = petBirthdayTextView.getText().toString();
        String pricePet = petPriceTextView.getText().toString();

        if(petName.isEmpty()){
            Toast.makeText(this, "Pet name should not be empty", Toast.LENGTH_SHORT).show();
            return;
        }else if (petName.length() < 2){
            petNameEdit.requestFocus();
            Toast.makeText(this, "Pet should have name at least 2 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if(description.isEmpty()){
            Toast.makeText(this, "Description should not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (description.length() < 20){
            petNameEdit.requestFocus();
            Toast.makeText(this, "Description must have 20 characters and above", Toast.LENGTH_SHORT).show();
            return;
        }

        if(colorMarkings.isEmpty()){
            Toast.makeText(this, "Color/Markings should not be empty", Toast.LENGTH_SHORT).show();
            return;
        }else if(colorMarkings.length() < 3){
            Toast.makeText(this, "Color/Markings must have 3 characters and above", Toast.LENGTH_SHORT).show();
            return;
        }

        if(breedCategory.equals("Breed")){
            Toast.makeText(this, "Please choose the specific breed of your pet", Toast.LENGTH_SHORT).show();
            return;
        }


        if(added_vaccine ==null || added_vaccine.getChildCount() ==0){
            Toast.makeText(this, "Add vaccine or medicine at least 1", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            String showAllPrompt = "";
            View row;
            ArrayList<String> list = new ArrayList<>();
            int countView = added_vaccine.getChildCount();
            for(int i = 0; i< countView; i++){
               row =added_vaccine.getChildAt(i);
                TextView edit = row.findViewById(R.id.vaccine_save_textout);
                TextView count = row.findViewById(R.id.vaccine_save_count);
                String text = edit.getText().toString();
                String countNo = count.getText().toString();
                saveVaccine.addAll(Arrays.asList(text+":"+countNo));
                   showAllPrompt += "Name"+edit.getText().toString()+" no. of times:"+count.getText().toString()+"\n";
            }

/*            //for printing

                for(int i = 0; i<saveVaccine.size();i++) {
                    Toast.makeText(this,saveVaccine.get(0), Toast.LENGTH_SHORT).show();

        }*/
  }
        if(genderPet.equals("Gender")){
            Toast.makeText(this, "Please choose the specific gender of your pet", Toast.LENGTH_SHORT).show();
            return;
        }

        if(birthdayPet.equals("Birthday")){
            Toast.makeText(this, "Please set the birthday of your pet", Toast.LENGTH_SHORT).show();
            return;
        }

        if(pricePet.equals("Price")){
            Toast.makeText(this, "Please set the price for your pet", Toast.LENGTH_SHORT).show();
            return;
        }
        if(uri.size() == 0){
            Toast.makeText(this, "Please provide a picture of your pet", Toast.LENGTH_SHORT).show();
            return;
        }




        progressDialog.setTitle("Creating your pet details");
        progressDialog.setMessage("Please wait, while we are setting your pet data..");

        progressDialog.show();
        petClass petClass = new petClass(petName,description,colorMarkings,breedCategory,saveVaccine.toString(),genderPet,birthdayPet,pricePet
                                                ,"",firebaseUser.getUid(),true);

        firebaseDatabase.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Pet").exists()){
                    countID = (int) snapshot.child("Pet").getChildrenCount()+1;

                    //for Breeder --> Breeder Shop ---> Pet
                    databaseReference.child("Pet").child("Pet"+countID).setValue(petClass);
                    //for Pet parent node
                    firebaseDatabase.getReference("Pet").child("Pet"+countID).setValue(petClass);
                    //for Shop parent
                   firebaseDatabase.getReference("Shop").child(firebaseUser.getUid()).child("Pet").child("Pet" + countID).setValue(petClass);
                   //for storage
                    uploadToFirebaseStorage(countID);


                        Toast.makeText(pet_add.this, "Successfully Created", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(progressDialog.isShowing())
                                    progressDialog.dismiss();
                                countID =1;
                                uri.clear();
                                saveVaccine.clear();
                                transistion();
                            }
                        },2000);


                }else{
                    //to save image from firebase storage

                    //for Breeder --> Breeder Shop ---> Pet
                    databaseReference.child("Pet").child("Pet"+countID).setValue(petClass);
                    //for Pet parent node
                    firebaseDatabase.getReference("Pet").child("Pet"+countID).setValue(petClass);
                    //for Shop parent
                    firebaseDatabase.getReference("Shop").child(firebaseUser.getUid()).child("Pet").child("Pet" + countID).setValue(petClass);
                    //for storage
                    uploadToFirebaseStorage(countID);

                        Toast.makeText(pet_add.this, "Successfully Created", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(progressDialog.isShowing())
                                    progressDialog.dismiss();
                                countID =1;
                                uri.clear();
                                saveVaccine.clear();
                                transistion();
                            }
                        },2000);
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void clicked(int size) {
        Toast.makeText(this,uri.size()+": selected",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemClick(int position) {
        Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.custom_dialog_zoom);

        TextView textView = dialog.findViewById(R.id.text_dialog);
        ImageView imageView = dialog.findViewById(R.id.image_view_dialog);
        Button buttonClose = dialog.findViewById(R.id.button_close_dialog);


        textView.setText("Image"+position);
        imageView.setImageURI(uri.get(position));
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private final TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

            arrayAdapter.getFilter().filter(s);

        }
    };
    private final TextWatcher petNameTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @SuppressLint("SetTextI18n")
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            petNameCount.setText(s.length() +"/30");
            if(s.length()!=0){
                petNameClear.setVisibility(View.VISIBLE);
                petNameClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        petNameEdit.getText().clear();
                    }
                });
            }
            else{
                petNameClear.setVisibility(View.GONE);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };
    private final TextWatcher petDescTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @SuppressLint("SetTextI18n")
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            petDescCount.setText(s.length() +"/2000");
            if(s.length()!=0){
                petDescClear.setVisibility(View.VISIBLE);
                petDescClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        petDescEdit.getText().clear();
                    }
                });
            }
            else{
                petDescClear.setVisibility(View.GONE);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };
    private final TextWatcher petColorTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @SuppressLint("SetTextI18n")
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            petColorCount.setText(s.length() +"/200");
            if(s.length()!=0){
                petColorClear.setVisibility(View.VISIBLE);
                petColorClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        petColorEdit.getText().clear();
                    }
                });
            }
            else{
                petColorClear.setVisibility(View.GONE);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };
    private final TextWatcher addVaccineTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length()!=0){
                imageClear.setVisibility(View.VISIBLE);
                imageClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textIn.getText().clear();
                    }
                });
            }
            else{
                imageClear.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
}

package com.creatist.abhi.lifecalculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

public class Insight extends AppCompatActivity {

    private static final String fileName = "UserDB.txt";

    TextView insightUserName, insightHoroscopeSign;
    TextView dataBirthDetails, dataBirthDay;
    TextView dataDays, dataMonths, dataWeek;
    TextView dataHour, dataMinutes, dataSeconds;
    TextView age;

    Button deleteButton;

    ImageView imageHoroscope;

    Boolean showAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insight);

        showAds = false;

        // Initializing the TextViews
        imageHoroscope = (ImageView) findViewById(R.id.imageHoroscope);
        insightUserName = findViewById(R.id.insightUserName);
        insightHoroscopeSign = findViewById(R.id.insightHoroscopeSign);
        dataBirthDetails = findViewById(R.id.dateOfBirthInsight);
        dataBirthDay = findViewById(R.id.dayOfTheWeekInsight);
        dataDays = findViewById(R.id.daysCompletedInsight);
        dataMonths = findViewById(R.id.monthsCompletedInsight);
        dataWeek = findViewById(R.id.weeksCompletedInsight);
        dataHour = findViewById(R.id.hoursCompletedInsight);
        dataMinutes = findViewById(R.id.minutesCompletedInsight);
        dataSeconds = findViewById(R.id.secondsCompletedInsight);
        age = findViewById(R.id.ageInsight);
        deleteButton = findViewById(R.id.deleteButton);

        // need a value from ManActivity in a bundle.
        int id = getIdFromBundle();

        // reading from the DB
        String temp = readFromFile();

        // finding id and getting data
        String data[] = temp.split("-");
        String dataToWork[] = new String[2];

        try {
            int x = id;
            for (int i = 0; i < data.length - 1; i += 3) {
                String g = Integer.toString(x);
                String s = data[i];
                if (g.compareTo(s) == 0) {
                    dataToWork[0] = data[i + 1];              // Name
                    dataToWork[1] = data[i + 2];              // DOB
                } else {
                    //Toast.makeText(this, "Error: ID Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Exception", Toast.LENGTH_SHORT).show();
        }

        // segregating and parsing variable data
        String dataDOB[] = dataToWork[1].split("/");
        int birthDay = Integer.parseInt(dataDOB[0]);
        int birthMonth = Integer.parseInt(dataDOB[1]);
        int birthYear = Integer.parseInt(dataDOB[2]);

        // getting current day details
        int currentDay, currentMonth, currentYear;
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        currentMonth++;

        //calculating work
        int receivingArray[] = countDays(birthDay, birthMonth, birthYear, currentDay, currentMonth, currentYear);

        // display the final result
        insightUserName.setText(dataToWork[0].toString());
        insightHoroscopeSign.setText(calculateHoroscopeSign(birthMonth, birthDay));
        imageHoroscope.setScaleType(ImageView.ScaleType.CENTER_CROP);

        dataBirthDetails.setText(monthOfTheYearInString(birthMonth) + " " + dataDOB[0].toString() + ", " + dataDOB[2].toString());
        dataBirthDay.setText(dayOfTheWeek(birthDay, birthMonth, birthYear));
        dataDays.setText(Integer.toString(receivingArray[0]));
        dataMonths.setText(Integer.toString(receivingArray[1]));
        dataWeek.setText(Integer.toString(receivingArray[1] * 4));
        dataHour.setText(Integer.toString(receivingArray[0] * 24));
        dataMinutes.setText(Integer.toString(receivingArray[0] * 24 * 60));
        dataSeconds.setText(Integer.toString(receivingArray[0] * 24 * 60 * 60));
        age.setText(Integer.toString(calculateAge(birthDay, birthMonth, birthYear, currentDay, currentMonth, currentYear)) + " YEARS");


        // DELETE BUTTON

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Insight.this)
                        .setMessage("Are you sure?")

                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // DELETING THE LIFE RECORD

                                int id = getIdFromBundle();
                                String temp = readFromFile();

                                // finding id and getting data
                                String data[] = temp.split("-");

                                String finalDataToEdit = "";

                                // Eliminating the current ID

                                try {
                                    int idCopy = id;                                                // ID Copy
                                    String idCopyString = Integer.toString(idCopy);                 // ID Copy String
                                    for (int i = 0; i < data.length - 1; i += 3) {
                                        String currentIdInMemory = data[i];                         // got current ID
                                        if (idCopyString.compareTo(currentIdInMemory) == 0) {       // comparing both String IDs
                                            // found the ID
                                            // No need to do anything.
                                        } else {
                                            // check the previous ID
                                            finalDataToEdit += data[i] + "-" + data[i + 1] + "-" + data[i + 2] + "-";
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_SHORT).show();
                                }

                                // Editing IDs in finalDataToEdit.

                                String[] dataSeparate = finalDataToEdit.split("-");
                                String finalDataToWrite = "";
                                int designatedId = 1;

                                for (int i = 0; i < dataSeparate.length - 1; i += 3) {
                                    dataSeparate[i] = Integer.toString(designatedId);
                                    designatedId++;
                                    finalDataToWrite += dataSeparate[i] + "-" + dataSeparate[i + 1] + "-" + dataSeparate[i + 2] + "-";
                                }
                                writeToFile(finalDataToWrite);

                                Toast.makeText(Insight.this, "Life Record Deleted", Toast.LENGTH_SHORT).show();

                                Intent myIntent = new Intent(Insight.this, MainActivity.class);
                                startActivity(myIntent);

                            }
                        })

                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        });
                alertDialog.show();
            }
        });


        // AD SETUP START

        if (!this.showAds) {
            AdView adView = findViewById(R.id.adView2);
            adView.loadAd(new AdRequest.Builder().build());
        }

        // AD SETUP END
    }

    @Override
    public void onBackPressed() {
        this.finish();

    }


    // SUPPORTING FUNCTIONS

    void writeToFile(String data) {
        String text = data;
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = openFileOutput(fileName, MODE_PRIVATE);
            fileOutputStream.write(text.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    String readFromFile() {
        String temp = "";
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String text;
            while ((text = bufferedReader.readLine()) != null) {
                temp += text;
            }
            return temp;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return temp;
    }

    // Main Methods

    public String calculateHoroscopeSign(int birthMonth, int birthDay) {

        if (birthMonth == 3) {
            if (birthDay >= 21) {
                imageHoroscope.setImageResource(R.drawable.aries);
                return "Aries";
            } else {
                imageHoroscope.setImageResource(R.drawable.pisces);
                return "Pisces";
            }
        } else if (birthMonth == 4) {
            if (birthDay >= 20) {
                imageHoroscope.setImageResource(R.drawable.taurus);
                return "Taurus";
            } else {
                imageHoroscope.setImageResource(R.drawable.aries);
                return "Aries";
            }
        } else if (birthMonth == 5) {
            if (birthDay >= 21) {
                imageHoroscope.setImageResource(R.drawable.gemini);
                return "Gemini";
            } else {
                imageHoroscope.setImageResource(R.drawable.taurus);
                return "Taurus";
            }
        } else if (birthMonth == 6) {
            if (birthDay >= 21) {
                imageHoroscope.setImageResource(R.drawable.cancer);
                return "Cancer";
            } else {
                imageHoroscope.setImageResource(R.drawable.gemini);
                return "Gemini";
            }
        } else if (birthMonth == 7) {
            if (birthDay >= 23) {
                imageHoroscope.setImageResource(R.drawable.leo);
                return "Leo";
            } else {
                imageHoroscope.setImageResource(R.drawable.cancer);
                return "Cancer";
            }
        } else if (birthMonth == 8) {
            if (birthDay >= 23) {
                imageHoroscope.setImageResource(R.drawable.virgo);
                return "Virgo";
            } else {
                imageHoroscope.setImageResource(R.drawable.leo);
                return "Leo";
            }
        } else if (birthMonth == 9) {
            if (birthDay >= 23) {
                imageHoroscope.setImageResource(R.drawable.libra);
                return "Libra";
            } else {
                imageHoroscope.setImageResource(R.drawable.virgo);
                return "Virgo";
            }
        } else if (birthMonth == 10) {
            if (birthDay >= 23) {
                imageHoroscope.setImageResource(R.drawable.scorpio);
                return "Scorpio";
            } else {
                imageHoroscope.setImageResource(R.drawable.libra);
                return "Libra";
            }
        } else if (birthMonth == 11) {
            if (birthDay >= 22) {
                imageHoroscope.setImageResource(R.drawable.sagittarius);
                return "Sagittarius";
            } else {
                imageHoroscope.setImageResource(R.drawable.scorpio);
                return "Scorpio";
            }
        } else if (birthMonth == 12) {
            if (birthDay >= 22) {
                imageHoroscope.setImageResource(R.drawable.capricorn);
                return "Capricorn";
            } else {
                imageHoroscope.setImageResource(R.drawable.sagittarius);
                return "Sagittarius";
            }
        } else if (birthMonth == 1) {
            if (birthDay >= 20) {
                imageHoroscope.setImageResource(R.drawable.aquarius);
                return "Aquarius";
            } else {
                imageHoroscope.setImageResource(R.drawable.capricorn);
                return "Capricorn";
            }
        } else if (birthMonth == 2) {
            if (birthDay >= 19) {
                imageHoroscope.setImageResource(R.drawable.pisces);
                return "Pisces";
            } else {
                imageHoroscope.setImageResource(R.drawable.aquarius);
                return "Aquarius";
            }
        } else {
            return "Unable to find";
        }
    }

    int calculateAge(int birthDay, int birthMonth, int birthYear, int currentDay, int currentMonth, int currentYear) {
        int year = currentYear - birthYear;
        if (currentMonth > birthMonth) {
            return year;
        } else if (currentMonth == birthMonth) {
            if (currentDay > birthDay || currentDay == birthDay) {
                return year;
            } else {
                return --year;
            }
        } else {
            return --year;
        }
    }

    int[] countDays(int birthDay, int birthMonth, int birthYear, int currentDay, int currentMonth, int currentYear) {
        int[] valueReturn = new int[2];
        int totalDays = 0, totalMonths = 0;
        for (int year = birthYear; year <= currentYear; year++) {
            //System.out.println("year:" + year);
            for (int month = monthOfTheYear(year, birthMonth, birthYear); month <= monthInYear(month, year, currentMonth, currentYear); month++) {
                //System.out.print("month: " + month + " Days: ");
                for (int day = firstDayOfTheMonth(month, year, birthDay, birthMonth, birthYear); day <= totalDaysInMonth(month, year, currentDay, currentMonth, currentYear); day++) {
                    //System.out.print(day + " ");
                    totalDays++;
                }
                totalMonths++;
                //System.out.println();
            }
            //System.out.println();
        }
        totalMonths--;
        valueReturn[0] = totalDays;
        valueReturn[1] = totalMonths;
        return valueReturn;
    }

    // sub-supporting methods

    int monthOfTheYear(int year, int birthMonth, int birthYear) {
        if (year == birthYear) {
            return birthMonth;
        } else {
            return 1;
        }
    }

    int monthInYear(int month, int year, int currentMonth, int currentYear) {
        if (year == currentYear) {
            if (month == (currentMonth + 1)) {
                return 0;
            } else {
                return month;
            }
        } else {
            if (month >= 13) {
                return 0;
            } else {
                return month;
            }
        }
    }

    int firstDayOfTheMonth(int month, int year, int birthDay, int birthMonth, int birthYear) {
        if (year == birthYear) {
            if (month == birthMonth) {
                return ++birthDay;
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }

    int totalDaysInMonth(int month, int year, int currentDay, int currentMonth, int currentYear) {
        if (year == currentYear) {
            if (month == currentMonth) {
                return currentDay;
            } else {
                return daysInMonth(month, year);
            }
        } else {
            return daysInMonth(month, year);
        }
    }

    int daysInMonth(int month, int year) {
        if (month > 7) {
            if (month % 2 == 0) {
                return 31;
            } else {
                return 30;
            }
        } else {
            if (month == 2) {
                if (isLeapYear(year)) {
                    return 29;
                } else {
                    return 28;
                }
            } else {
                if (month % 2 == 0) {
                    return 30;
                } else {
                    return 31;
                }
            }
        }

    }

    boolean isLeapYear(int year) {
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                year /= 100;
                if (year % 4 == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public String dayOfTheWeek(int birthday, int birthMonth, int birthYear) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, birthYear);
        c.set(Calendar.MONTH, --birthMonth);
        c.set(Calendar.DATE, birthday);
        int day = c.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            case Calendar.SUNDAY:
                return "Sunday";
            default:
                return "Not Found";
        }
    }

    public String monthOfTheYearInString(int month) {
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "Month not found";
        }
    }

    public int getIdFromBundle() {
        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("ID");
        return ++id;
    }
}

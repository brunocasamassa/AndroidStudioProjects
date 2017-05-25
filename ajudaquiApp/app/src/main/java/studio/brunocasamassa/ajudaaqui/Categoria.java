package studio.brunocasamassa.ajudaaqui;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;

/**
 * Created by bruno on 22/05/2017.
 */

class Categoria {
    public ArrayList<String> getCategorias() {
        return tags;
    }

    private ArrayList<String> tags;


    public void setCategorias(ArrayList<String> categorias) {
        this.tags = categorias;
    }


    public void save() {
        DatabaseReference referenciaFirebase = FirebaseConfig.getFireBase();
        referenciaFirebase.child("tags").setValue(this);
    }


}

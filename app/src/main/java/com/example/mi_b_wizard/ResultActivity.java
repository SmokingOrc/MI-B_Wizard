package com.example.mi_b_wizard;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.example.mi_b_wizard.Data.Player;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

public class ResultActivity extends AppCompatActivity {
    List<Matrix> resultList;
    List<Matrix> playerList;
    int rows, columns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //test spalten und zeilen
        rows=2;
        columns=4;

        GridView players=(GridView) findViewById(R.id.players);
        players.setAdapter(new MatricAdapter(getApplicationContext(),playerList));

        List<Matrix> playerList=new ArrayList<>();
        for (int i=0;i<rows;i++)
        {
            for (int j=0;j<columns;j++)
            {
                playerList.add(new Matrix(i,j));
            }
        }

        GridView result = (GridView) findViewById(R.id.result);
        result.setAdapter(new MatricAdapter(this,resultList));
        result.setNumColumns(columns);
    }
    public class Matrix {
        int i;
        int j;

        public Matrix(int i, int j){
            this.i=i;
            this.j=j;
        }

    }

    public class MatricAdapter extends BaseAdapter{
        Context context;
        List <Matrix> resultList;

        public MatricAdapter(Context context, List<Matrix> resultList){
            this.context=context;
            this.resultList=resultList;
        }


        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public Object getItem(int i) {
            return resultList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup parent) {
            View v = View.inflate(context,R.layout.griditem,null);
            return null;
        }
    }
}

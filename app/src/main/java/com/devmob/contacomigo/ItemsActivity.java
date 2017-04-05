package com.devmob.contacomigo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.devmob.contacomigo.ExpandableList.ChildInfo;
import com.devmob.contacomigo.ExpandableList.ExpandableListAdapter;
import com.devmob.contacomigo.ExpandableList.HeaderInfo;
import com.devmob.contacomigo.model.Food;
import com.devmob.contacomigo.model.Person;

import java.util.ArrayList;
import java.util.LinkedHashMap;


public class ItemsActivity extends AppCompatActivity {

    private LinkedHashMap<String, HeaderInfo> productHash = new LinkedHashMap<String, HeaderInfo>();
    private ArrayList<HeaderInfo> productList = new ArrayList<HeaderInfo>();

    private ExpandableListAdapter listAdapter;
    private ExpandableListView itemsExpandableListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Inicialização
        loadData();
        itemsExpandableListView = (ExpandableListView) findViewById(R.id.simpleExpandableListView);
        listAdapter = new ExpandableListAdapter(ItemsActivity.this, productList);
        itemsExpandableListView.setAdapter(listAdapter);



        //LONG CLICK EM CADA CHILD (PESSOA E PREÇO)
        itemsExpandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);
                    //get the group header
                    HeaderInfo headerInfo = productList.get(groupPosition);
                    //get the child info
                    ChildInfo detailInfo = headerInfo.getProductList().get(childPosition);
                    Toast.makeText(ItemsActivity.this, detailInfo.getPersonName() + " deve " + detailInfo.getPrice(), Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            }
        });
        // CLICK EM CADA CHILD (PESSOA E PREÇO)
        itemsExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                HeaderInfo headerInfo = productList.get(groupPosition);
                ChildInfo detailInfo = headerInfo.getProductList().get(childPosition);
                //Toast.makeText(getBaseContext(), " Clicked on :: " + headerInfo.getName()+ "/" + detailInfo.getPrice(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        // CLICK EM CADA HEADER (PRODUTO E PREÇO)
        itemsExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                HeaderInfo headerInfo = productList.get(groupPosition);
                //Toast.makeText(getBaseContext(), " Header is :: " + headerInfo.getName(),Toast.LENGTH_SHORT).show();
                return false;
            }
        });


    }

    //DATA SETADA POR HARDCODING TEMPORARIO
    private void loadData() {

        Person william = new Person("William", 1);
        Person silvio = new Person("Silvio", 2);
        Person daniel = new Person("Daniel", 3);


        Food batata = new Food(1, "Aussie Cheese Fries", 48.60);
        Food cebola = new Food(2, "Bloomin'Onion", 48.60);

        addProduct(william, batata);
        addProduct(silvio, batata);
        addProduct(daniel, batata);

        addProduct(william, cebola);
        addProduct(daniel, cebola);

    }

    //PREENCHIMENTO DE CLASSES
    private int addProduct(Person personO, Food food) {

        String product = food.getName();
        String person = personO.getName();
        double price = food.getPrice();

        int groupPosition = 0;

        //CHECA SE PRODUTO JA EXISTE
        HeaderInfo headerInfo = productHash.get(product);

        //CASO NÃO EXISTA, CRIA
        if (headerInfo == null) {
            headerInfo = new HeaderInfo();
            headerInfo.setFood(food);
            headerInfo.setFoodName(product);
            productHash.put(product, headerInfo);
            productList.add(headerInfo);
        }

        //get the children for the group
        ArrayList<ChildInfo> productList = headerInfo.getProductList();
        //size of the children list
        int listSize = productList.size();
        //add to the counter
        listSize++;

        //create a new child and add that to the group
        ChildInfo detailInfo = new ChildInfo();
        detailInfo.setPerson(personO);
        detailInfo.setPersonName(person);
        detailInfo.setPrice(price);
        productList.add(detailInfo);
        headerInfo.setPeopleList(productList);

        //find the group position inside the list
        groupPosition = this.productList.indexOf(headerInfo);
        return groupPosition;
    }


    //method to expand all groups
    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            itemsExpandableListView.expandGroup(i);
        }
    }

    //method to collapse all groups
    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            itemsExpandableListView.collapseGroup(i);
        }
    }
}

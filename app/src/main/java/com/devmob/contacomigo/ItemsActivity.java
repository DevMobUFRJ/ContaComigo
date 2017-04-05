package com.devmob.contacomigo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

    private LinkedHashMap<String, HeaderInfo> subjects = new LinkedHashMap<String, HeaderInfo>();
    private ArrayList<HeaderInfo> deptList = new ArrayList<HeaderInfo>();

    private ExpandableListAdapter listAdapter;
    private ExpandableListView simpleExpandableListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        // add data for displaying in expandable list view
        loadData();

        //get reference of the ExpandableListView
        simpleExpandableListView = (ExpandableListView) findViewById(R.id.simpleExpandableListView);
        // create the adapter by passing your ArrayList data
        listAdapter = new ExpandableListAdapter(ItemsActivity.this, deptList);
        // attach the adapter to the expandable list view
        simpleExpandableListView.setAdapter(listAdapter);

        //expand all the Groups
        //expandAll();

        // setOnChildClickListener listener for child row click
        simpleExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //get the group header
                HeaderInfo headerInfo = deptList.get(groupPosition);
                //get the child info
                ChildInfo detailInfo =  headerInfo.getProductList().get(childPosition);
                //display it or do something with it
                Toast.makeText(getBaseContext(), " Clicked on :: " + headerInfo.getFoodName()
                        + "/" + detailInfo.getPrice(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        // setOnGroupClickListener listener for group heading click
        simpleExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //get the group header
                HeaderInfo headerInfo = deptList.get(groupPosition);
                //display it or do something with it
                Toast.makeText(getBaseContext(), " Header is :: " + headerInfo.getFoodName(),
                        Toast.LENGTH_SHORT).show();

                return false;
            }
        });


    }

    //method to expand all groups
    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            simpleExpandableListView.expandGroup(i);
        }
    }

    //method to collapse all groups
    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            simpleExpandableListView.collapseGroup(i);
        }
    }

    //load some initial data into out list
    private void loadData(){

        Person william = new Person("William", 1);
        Person silvio = new Person("Silvio", 2);
        Person daniel = new Person("Daniel", 3);



        Food batata = new Food(1, "Aussie Cheese Fries", 48.60);
        Food cebola = new Food(2, "Bloomin'Onion", 48.60);

        addProduct2(william, batata);
        addProduct2(silvio, batata);
        addProduct2(daniel, batata);

        addProduct2(william, cebola);
        addProduct2(daniel, cebola);


        //----------------antigo-----------
        /*
        addProduct("Batata", "William","11,00");
        addProduct("Batata", "Silvio","11,00");
        addProduct("Batata", "Daniel","11,00");

        addProduct("Cebola", "William","14,00");
        addProduct("Cebola", "Silvio","14,00");
        */
    }


    //here we maintain our products in various departments
    private int addProduct2(Person personO, Food food){
        //    TODO
        //Adicionar parametro de preço, passar o mesmo nos metodos acima, trocar sequence para name, e o name para preço

        String product = food.getName();
        String person = personO.getName();
        double price = food.getPrice();

        int groupPosition = 0;

        //check the hash map if the group already exists
        HeaderInfo headerInfo = subjects.get(product);
        //add the group if doesn't exists
        if(headerInfo == null){
            headerInfo = new HeaderInfo();
            headerInfo.setFood(food);
            headerInfo.setFoodName(product);
            subjects.put(product, headerInfo);
            deptList.add(headerInfo);
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
        groupPosition = deptList.indexOf(headerInfo);
        return groupPosition;
    }



    //here we maintain our products in various departments
    private int addProduct(String product, String person, String price){
        //    TODO
        //Adicionar parametro de preço, passar o mesmo nos metodos acima, trocar sequence para name, e o name para preço

        int groupPosition = 0;

        //check the hash map if the group already exists
        HeaderInfo headerInfo = subjects.get(product);
        //add the group if doesn't exists
        if(headerInfo == null){
            headerInfo = new HeaderInfo();
            headerInfo.setFoodName(product);
            subjects.put(product, headerInfo);
            deptList.add(headerInfo);
        }

        //get the children for the group
        ArrayList<ChildInfo> productList = headerInfo.getProductList();
        //size of the children list
        int listSize = productList.size();
        //add to the counter
        listSize++;

        //create a new child and add that to the group
        ChildInfo detailInfo = new ChildInfo();
        detailInfo.setPersonName(person);
        detailInfo.setPrice(2.0);
        productList.add(detailInfo);
        headerInfo.setPeopleList(productList);

        //find the group position inside the list
        groupPosition = deptList.indexOf(headerInfo);
        return groupPosition;
    }
}

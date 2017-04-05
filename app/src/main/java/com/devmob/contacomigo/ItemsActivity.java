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

import java.util.ArrayList;
import java.util.LinkedHashMap;


public class ItemsActivity extends AppCompatActivity {

    private LinkedHashMap<String, HeaderInfo> subjects = new LinkedHashMap<String, HeaderInfo>();
    private ArrayList<HeaderInfo> prodList = new ArrayList<HeaderInfo>();

    private ExpandableListAdapter listAdapter;
    private ExpandableListView itemsExpandableListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // add data for displaying in expandable list view
        loadData();

        //get reference of the ExpandableListView
        itemsExpandableListView = (ExpandableListView) findViewById(R.id.simpleExpandableListView);
        // create the adapter by passing your ArrayList data
        listAdapter = new ExpandableListAdapter(ItemsActivity.this, prodList);
        // attach the adapter to the expandable list view
        itemsExpandableListView.setAdapter(listAdapter);

        //expand all the Groups
        //expandAll();
        itemsExpandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);
                    //get the group header
                    HeaderInfo headerInfo = prodList.get(groupPosition);
                    //get the child info
                    ChildInfo detailInfo =  headerInfo.getProductList().get(childPosition);
                    Toast.makeText(ItemsActivity.this, detailInfo.getPerson() + " deve " + detailInfo.getPrice(), Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            }
        });
        // setOnChildClickListener listener for child row click
        itemsExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //get the group header
                HeaderInfo headerInfo = prodList.get(groupPosition);
                //get the child info
                ChildInfo detailInfo =  headerInfo.getProductList().get(childPosition);
                //display it or do something with it
                //Toast.makeText(getBaseContext(), " Clicked on :: " + headerInfo.getName()
                //        + "/" + detailInfo.getPrice(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        // setOnGroupClickListener listener for group heading click
        itemsExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //get the group header
                HeaderInfo headerInfo = prodList.get(groupPosition);
                //display it or do something with it
                //Toast.makeText(getBaseContext(), " Header is :: " + headerInfo.getName(),
                //        Toast.LENGTH_SHORT).show();

                return false;
            }
        });


    }

    //method to expand all groups
    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            itemsExpandableListView.expandGroup(i);
        }
    }

    //method to collapse all groups
    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            itemsExpandableListView.collapseGroup(i);
        }
    }

    //load some initial data into out list
    private void loadData(){

        addProduct("Batata", "William","11,00");
        addProduct("Batata", "Silvio","11,00");
        addProduct("Batata", "Daniel","11,00");

        addProduct("Cebola", "William","14,00");
        addProduct("Cebola", "Silvio","14,00");

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
            headerInfo.setName(product);
            subjects.put(product, headerInfo);
            prodList.add(headerInfo);
        }

        //get the children for the group
        ArrayList<ChildInfo> productList = headerInfo.getProductList();
        //size of the children list
        int listSize = productList.size();
        //add to the counter
        listSize++;

        //create a new child and add that to the group
        ChildInfo detailInfo = new ChildInfo();
        detailInfo.setPerson(person);
        detailInfo.setPrice(price);
        productList.add(detailInfo);
        headerInfo.setProductList(productList);

        //find the group position inside the list
        groupPosition = prodList.indexOf(headerInfo);
        return groupPosition;
    }
}

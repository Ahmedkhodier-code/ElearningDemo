package sci.khodier.andriod.elearningdemo;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class frag_ActivityStream extends Fragment {

    Context context;
    FirebaseUser currentUser;
    ArrayList<announcements> myListData = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "ReadAndWriteSnippets";
    AppCompatButton ann, task;
    View rootview;

    frag_ActivityStream(Context context, FirebaseUser currentUser) {
        this.context = context;
        this.currentUser = currentUser;
    }

    public void getAnn() {
        db.collection("announcements")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        myListData.add(new announcements(document.getString("message"),
                                document.get("timestamp")+"" ,document.getString("courseName"), "announcements"));
                        System.out.println("-------------------/////----------------");
                    }
                    RecyclerView recyclerView = rootview.findViewById(R.id.AnnAndTask);
                    AnnTaskAdapter adapter = new AnnTaskAdapter(myListData, getContext());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                    Toast.makeText(getContext(), "Student failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootview = inflater.inflate(R.layout.frag__activity_stream, container, false);
        ann = rootview.findViewById(R.id.annBtn);
        task = rootview.findViewById(R.id.taskBtn);
        ann.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ann.setTextColor(getResources().getColor(R.color.colorAccent));
                task.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setTextColor(getResources().getColor(R.color.colorAccent));
                ann.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        getAnn();
        RecyclerView recyclerView = rootview.findViewById(R.id.AnnAndTask);
        AnnTaskAdapter adapter = new AnnTaskAdapter(myListData, getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return rootview;
    }
}
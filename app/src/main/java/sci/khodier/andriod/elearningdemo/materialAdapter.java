package sci.khodier.andriod.elearningdemo;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;


public class materialAdapter extends RecyclerView.Adapter<materialAdapter.ViewHolder> {
    private ArrayList<material> listdata;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private static final String TAG = "ReadAndWriteSnippets";
    Context context;
    String place;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    DocumentReference ref;
    String role = "";
    String uploader;

    // RecyclerView recyclerView;
    public materialAdapter(ArrayList<material> listdata, Context context, String place, String role) {
        this.listdata = listdata;
        this.place = place;
        this.context = context;
        this.role = role;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item2, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final material currentCourse = listdata.get(position);
        holder.cousreName.setText(listdata.get(position).getName());
        holder.time.setText(listdata.get(position).getTime());
        String materialName = listdata.get(position).getName();
        String matId = listdata.get(position).getMaterialId();
        holder.creator.setText(listdata.get(position).getUsername());
        String materialId = listdata.get(position).getId();
        String courseId = listdata.get(position).getCourseId();

        if (role == "Student" || role.equals("Student")) {
            if(place.equals("mat")){
                holder.del.setVisibility(View.GONE);
            }
        }
        int idx = position;
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog)
                        .setTitle("Delete File")
                        .setMessage("Are you sure, you want to delete this file?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (place == "mat") {
                                    FirebaseFirestore.getInstance().collection("courses")
                                            .document(courseId).collection("material").
                                            document(materialId).delete().
                                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        System.out.println("courseId: " + courseId);
                                                        System.out.println("materialId: " + materialId);
                                                        listdata.remove(idx);
                                                        Toast.makeText(context, "Deleted Successfully",
                                                                Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(context, "Failed to delete",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else if (place == "ass") {
                                    FirebaseFirestore.getInstance().collection("tasks")
                                            .document(courseId).collection("material").
                                            document(materialId).delete().
                                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        System.out.println("courseId: " + courseId);
                                                        System.out.println("materialId: " + materialId);
                                                        listdata.remove(idx);
                                                        Toast.makeText(context, "Deleted Successfully",
                                                                Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(context, "Failed to delete",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }

                            }

                        })
                        .setNegativeButton("Cancel", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("matId"+matId);
                storageRef.child(matId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri.toString()));
                        context.startActivity(browserIntent);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "application/*");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        // Got the download URL for 'users/me/profile.png'
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        System.out.println(exception.toString());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cousreName, time , creator;
        public RelativeLayout relativeLayout;
        public Button del;

        public ViewHolder(View itemView) {
            super(itemView);
            del = itemView.findViewById(R.id.deleteBtn);
            this.creator=(TextView)itemView.findViewById(R.id.creator);
            this.cousreName = (TextView) itemView.findViewById(R.id.name);
            this.time = itemView.findViewById(R.id.time);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
        }
    }

}
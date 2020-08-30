package com.geraud.quizzapp.Feature;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import com.geraud.quizzapp.Adapter.QuizListAdapter;
import com.geraud.quizzapp.Model.Category;
import com.geraud.quizzapp.R;
import com.geraud.quizzapp.ViewModel.QuizListViewModel;

import java.util.List;


public class ListFragment extends Fragment implements QuizListAdapter.OnQuizListItemClicked {

    private NavController navController;

    private RecyclerView recyclerView;
    private QuizListViewModel quizListViewModel;

    private QuizListAdapter adapter;
    private List<Category> categorys;
    private ProgressBar listProgress;

    private Animation fadeInAnim;
    private Animation fadeOutAnim;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        recyclerView = view.findViewById(R.id.list_view);
        listProgress = view.findViewById(R.id.list_progress);



        //animations
        fadeInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        fadeOutAnim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //here get quiz category list
        quizListViewModel = new ViewModelProvider(getActivity()).get(QuizListViewModel.class);
        quizListViewModel.getQuizListModelData().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                //set quiz list category items
                categorys = categories;
                Log.d("Start Fragment",categories.get(0).getImage());

                //instantiate recyclerview and set adapter

                adapter = new QuizListAdapter(ListFragment.this, categorys);

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(adapter);

                //show RecyclerView when data is available
                recyclerView.startAnimation(fadeInAnim);
                listProgress.startAnimation(fadeOutAnim);

                //notify adapter that data has been added
                adapter.notifyDataSetChanged();
            }
        });

    }


    @Override
    public void onItemClicked(Category category) {
        Log.d("Item Clicked",category.getName());
        ListFragmentDirections.ActionListFragmentToDetailsFragment listFragmentToDetailsFragment = ListFragmentDirections.actionListFragmentToDetailsFragment(category);
        navController.navigate(listFragmentToDetailsFragment);
    }
}

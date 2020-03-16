package com.geraud.quizzapp;


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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import java.util.List;


public class ListFragment extends Fragment implements QuizListAdapter.OnQuizListItemClicked {

    private NavController navController;

    private RecyclerView listView;
    private QuizListViewModel quizListViewModel;

    private QuizListAdapter adapter;
    private List<QuizListModel> quizListModel;
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

        listView = view.findViewById(R.id.list_view);
        adapter = new QuizListAdapter(this, quizListModel);
        listProgress = view.findViewById(R.id.list_progress);

        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setHasFixedSize(true);
        listView.setAdapter(adapter);

        fadeInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        fadeOutAnim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //here get quiz category list
        quizListViewModel = new ViewModelProvider(getActivity()).get(QuizListViewModel.class);
        quizListViewModel.getQuizListModelData().observe(getViewLifecycleOwner(), new Observer<List<QuizListModel>>() {
            @Override
            public void onChanged(List<QuizListModel> quizListModels) {
                //set quizlist category items
                quizListModel = quizListModels;

                //Load RecyclerView
                listView.startAnimation(fadeInAnim);
                listProgress.startAnimation(fadeOutAnim);

                adapter.notifyDataSetChanged();
            }
        });

    }


    @Override
    public void onItemClicked(QuizListModel quizListModel) {
        ListFragmentDirections.ActionListFragmentToDetailsFragment action = ListFragmentDirections.actionListFragmentToDetailsFragment(quizListModel);
        action.setQuizListModel(quizListModel);
        navController.navigate(action);
    }
}

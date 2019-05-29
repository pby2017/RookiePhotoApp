package kr.co.yogiyo.rookiephotoapp.diary.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import kr.co.yogiyo.rookiephotoapp.R;
import kr.co.yogiyo.rookiephotoapp.diary.db.Diary;
import kr.co.yogiyo.rookiephotoapp.diary.db.DiaryDatabaseCallback;
import kr.co.yogiyo.rookiephotoapp.diary.db.LocalDiaryManager;

public class DiariesFragment extends Fragment implements DiaryDatabaseCallback {

    private Context context;

    private DiariesAdapter diariesAdapter;

    private ProgressBar loadDiariesProgressBar;

    public static Fragment newInstance(DiariesActivity context, int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);

        return Fragment.instantiate(context, DiariesFragment.class.getName(), bundle);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isResumed() && isVisibleToUser) {
            loadDiaries();
        }
        // TODO: 이전에 데이터베이스 접근 또는 API 호출 이력이 있으면 무시하도록 구현
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        View root = inflater.inflate(R.layout.fragment_diaries, container, false);

        RecyclerView diariesRecyclerView = root.findViewById(R.id.recycler_diaries);
        loadDiariesProgressBar = root.findViewById(R.id.progressbar_load_diaries);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        diariesRecyclerView.setLayoutManager(linearLayoutManager);
        diariesAdapter = new DiariesAdapter(context, new ArrayList<Diary>());
        diariesRecyclerView.setAdapter(diariesAdapter);
        diariesRecyclerView.addItemDecoration(new DividerItemDecoration(context, linearLayoutManager.getOrientation()));

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            loadDiaries();
        }
    }

    @Override
    public void onDiariesBetweenDatesFinded(List<Diary> diaries) {
        diariesAdapter.setItems(diaries);
        diariesAdapter.notifyDataSetChanged();
        loadDiariesProgressBar.setVisibility(View.GONE);
    }

    private void loadDiaries() {
        if (this.getArguments() == null) {
            throw new NullPointerException();
        }
        int position = this.getArguments().getInt("position");
        Calendar calendar = DiariesActivity.getCalendar(position);

        if (calendar == null) {
            return;
        }

        Calendar fromCalendar = new GregorianCalendar();
        fromCalendar.setTime(calendar.getTime());
        Calendar toCalendar = new GregorianCalendar();
        toCalendar.setTime(calendar.getTime());

        fromCalendar.set(Calendar.DAY_OF_MONTH, 1);
        toCalendar.set(Calendar.DAY_OF_MONTH, 1);

        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        toCalendar.add(Calendar.MONTH, 1);
        toCalendar.add(Calendar.MILLISECOND, -1);

        loadDiariesProgressBar.setVisibility(View.VISIBLE);

        LocalDiaryManager.getInstance(context).findDiariesBetweenDates(this, fromCalendar.getTime(), toCalendar.getTime());
    }
}
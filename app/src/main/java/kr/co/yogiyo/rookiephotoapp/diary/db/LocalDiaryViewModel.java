package kr.co.yogiyo.rookiephotoapp.diary.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;

public class LocalDiaryViewModel extends AndroidViewModel {
    // TODO : 나중에 삭제
    private DiaryDatabase diaryDatabase;

    public LocalDiaryViewModel(@NonNull Application application) {
        super(application);
        diaryDatabase = DiaryDatabase.getDatabase(application);
    }

    public Completable insertDiary(final Date date, final String image, final String description) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() {
                Diary diary = new Diary(date, image, description);
                diaryDatabase.diaryDao().insertDiary(diary);
            }
        });
    }

    public Single<Diary> findDiaryById(final int diaryId) {
        return diaryDatabase.diaryDao().findDiaryById(diaryId);
    }

    // TODO : 날짜 순으로 정렬
    public Flowable<List<Diary>> findDiariesBetweenDates(Date from, Date to) {
        return diaryDatabase.diaryDao().findDiariesBetweenDates(from, to);
    }

    public Completable updateDiary(final Diary diary, final Date date, final String image, final String description) {
        diary.setDate(date);
        diary.setImage(image);
        diary.setDescription(description);

        return Completable.fromAction(new Action() {
            @Override
            public void run() {
                diaryDatabase.diaryDao().updateDiary(diary);
            }
        });
    }

    public Completable deleteDiary(final Diary diary) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() {
                diaryDatabase.diaryDao().deleteDiary(diary);
            }
        });
    }

}

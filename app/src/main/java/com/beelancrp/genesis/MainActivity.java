package com.beelancrp.genesis;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.beelancrp.genesis.adapter.MainAdapter;
import com.beelancrp.genesis.adapter.OnSearchItemClickListener;
import com.beelancrp.genesis.data.SearchItem;
import com.beelancrp.genesis.data.SerializableContainer;
import com.beelancrp.genesis.databinding.ActivityMainBinding;
import com.beelancrp.genesis.db.DataManager;
import com.beelancrp.genesis.thread.LoadDataListener;
import com.beelancrp.genesis.thread.WorkThread;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoadDataListener, OnSearchItemClickListener {

    private static final String SEARCH_TASK_1 = "SearchTask_1";
    private static final String SEARCH_TASK_2 = "SearchTask_2";
    private static final String EXTRA_KEY_SEARCH_ITEMS = "searchItems";
    private static final String EXTRA_KEY_SEARCH_TERM = "searchTerm";
    private static final String EXTRA_KEY_NEED_SEARCH = "needSearch";
    private static final String EXTRA_KEY_IS_CLEAR_NEED = "isClearNeed";
    private static final String EXTRA_KEY_IS_FIRST_START = "isFirstStart";

    private ActivityMainBinding mBinding;
    private List<SearchItem> items = new ArrayList<>();
    private MainAdapter adapter;
    private boolean isCleared = false;
    private String searchCode = "";
    private String apiKey = "";
    private String searchText = "";
    private DataManager mDataManager;
    private boolean isFirstStart = true;

    private WorkThread firstSearchThread, secondSearchThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mDataManager = new DataManager();
        if (isFirstStart) {
            isFirstStart = false;
            items = mDataManager.getResults();
            searchText = mDataManager.getTerm(1);
        }
        //Достаем из ресурсов @apiKy и @searchEngineCode для инициализации наших тредов
        searchCode = getString(R.string.google_search_code);
        apiKey = getString(R.string.google_custom_search_api_key);

        adapter = new MainAdapter(items, this);
        mBinding.mainActivityRecycler.setLayoutManager(new LinearLayoutManager(this));
        mBinding.mainActivityRecycler.setAdapter(adapter);

        /*
        Устанавливаем слушателя на кнопку поиска. По нажаитю на нее, будут создаватс наши рабочие
        треды и будет выполнен поиск.
         */
        mBinding.activityMainSearch.setOnSearchButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataManager.deleteAllData();
                searchText = mBinding.activityMainSearch.getText();
                startSearchTask(searchText);
                isCleared = true;
                mBinding.activityMainSearch.hideSearchButton();
                mBinding.activityMainSearch.showCancelButton();
            }
        });

        mBinding.activityMainSearch.setOnCancelButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSearchTasks();
                mBinding.activityMainSearch.showSearchButton();
                mBinding.activityMainSearch.hideCancelButton();
            }
        });
    }

    /*
        Утсанавливает полученные результаты в RecyclerView
     */
    private void updateUi(List<SearchItem> item) {
        if (isCleared) {
            items.clear();
            isCleared = false;
        }
        items.addAll(item);
        //Получаем доступ к главному потоку, что бы обновить данные в адаптере
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                mBinding.activityMainSearch.hideCancelButton();
                mBinding.activityMainSearch.showSearchButton();
            }
        });
        //Записывает полученный результат в базу данных
        mDataManager.saveResults(items);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (items.size() != 0) {
            outState.putSerializable(EXTRA_KEY_SEARCH_ITEMS, new SerializableContainer<>(items));
        }
        if (!searchText.isEmpty()) {
            outState.putString(EXTRA_KEY_SEARCH_TERM, searchText);
        }
        if (firstSearchThread != null | secondSearchThread != null) {
            outState.putBoolean(EXTRA_KEY_NEED_SEARCH, true);
            stopSearchTasks();
        }
        outState.putBoolean(EXTRA_KEY_IS_CLEAR_NEED, isCleared);
        outState.putBoolean(EXTRA_KEY_IS_FIRST_START, isFirstStart);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(EXTRA_KEY_SEARCH_ITEMS)) {
            SerializableContainer<SearchItem> tmp = (SerializableContainer<SearchItem>) savedInstanceState.getSerializable(EXTRA_KEY_SEARCH_ITEMS);
            updateUi(tmp.getList());
        }
        if (savedInstanceState.containsKey(EXTRA_KEY_SEARCH_TERM)) {
            searchText = savedInstanceState.getString(EXTRA_KEY_SEARCH_TERM);
        }
        if (savedInstanceState.containsKey(EXTRA_KEY_NEED_SEARCH)) {
            startSearchTask(searchText);
        }
        isCleared = savedInstanceState.getBoolean(EXTRA_KEY_IS_CLEAR_NEED);
        isFirstStart = savedInstanceState.getBoolean(EXTRA_KEY_IS_FIRST_START);
        super.onRestoreInstanceState(savedInstanceState);
    }

    /*
    Callback который срабатывает, когда тред УСПЕШНО закончил загрузку данных
    и готов нам что-то вернуть
     */
    @Override
    public synchronized void dataLoaded(List<SearchItem> result, String searchText) {
        updateUi(result);
        //Так как колбек может срабатывать не только от одного потока, мы закрываем только тот,
        //который в данный момент использует данный метод.
        stopSearchTasksByName(Thread.currentThread().getName());
    }

    /*
    Callback который срабатывает, когда в треде произошла ошибка(получения данных, нпе, списки)
     */
    @Override
    public synchronized void dataLoadFailed(Exception e) {
        e.printStackTrace();
        //Если была ошибка - закрываем наши потоки.
        stopSearchTasks();
    }

    /*
    Callback который срабатывает, когда по запросу пользователя не нашло результатов.
     */
    @Override
    public synchronized void noExistingData() {
        //Так же останавливаем потоки, что бы пользователь мог совершить повторный запрос.
        stopSearchTasks();
    }

    /*
    Запускает параллельный поиск в двух потоках с одним поисковым запросом.
     */
    private void startSearchTask(String searchText) {
        mDataManager.saveTerm(searchText);
        firstSearchThread = new WorkThread(searchCode, apiKey, 1, MainActivity.this);
        secondSearchThread = new WorkThread(searchCode, apiKey, 16, MainActivity.this);
        firstSearchThread.setName(SEARCH_TASK_1);
        secondSearchThread.setName(SEARCH_TASK_2);
        firstSearchThread.starWithParam(searchText);
        secondSearchThread.starWithParam(searchText);
    }

    /*
    *Выглядит не очень, но это пример остановки треда с официальной доки Оракла.
    */
    private void stopSearchTasks() {
        if (firstSearchThread != null)
            firstSearchThread = null;
        if (secondSearchThread != null)
            secondSearchThread = null;
    }

    private void stopSearchTasksByName(String threadName) {
        if (firstSearchThread != null && threadName.equals(SEARCH_TASK_1)) {
            firstSearchThread = null;
        }
        if (secondSearchThread != null && threadName.equals(SEARCH_TASK_2)) {
            secondSearchThread = null;
        }
    }

    @Override
    public void onSearchItemClicked(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(url));
        startActivity(browserIntent);
    }
}

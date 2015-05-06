package br.com.androidzin.brunomateus.beerstodrink.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.androidzin.brunomateus.beerstodrink.R;
import br.com.androidzin.brunomateus.beerstodrink.model.Statistics;

/**
 * Created by bruno on 05/05/15.
 */
public class StatisticsAdapter extends BaseAdapter {

    private ArrayList<Statistics> statistics;
    private LayoutInflater inflater;

    private static class ViewHolder {
        public final TextView country;
        public final TextView stats;
        public final ProgressBar progressBar;

        public ViewHolder(TextView country, TextView stats, ProgressBar progressBar) {
            this.country = country;
            this.stats = stats;
            this.progressBar = progressBar;
        }
    }

    public StatisticsAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setStatistics(ArrayList<Statistics> statistics){
        this.statistics = statistics;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return statistics.size();
    }

    @Override
    public Object getItem(int position) {
        return statistics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Statistics statistics = this.statistics.get(position);

        TextView country;
        TextView stats;
        ProgressBar progress;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.statistics_list_item, parent, false);
            country = (TextView) convertView.findViewById(R.id.stats_country_name);
            stats = (TextView) convertView.findViewById(R.id.stats_stats);
            progress = (ProgressBar) convertView.findViewById(R.id.stats_progressbar);
            convertView.setTag(new ViewHolder(country, stats, progress));
        } else{
            ViewHolder holder = (ViewHolder) convertView.getTag();
            country = holder.country;
            stats = holder.stats;
            progress = holder.progressBar;
        }

        country.setText(statistics.getCountry());
        stats.setText(statistics.getNumberOfDrankBeers()+"/"+statistics.getNumberOfBeers());

        progress.setMax(statistics.getNumberOfBeers());
        progress.setProgress(statistics.getNumberOfDrankBeers());


        return convertView;
    }
}

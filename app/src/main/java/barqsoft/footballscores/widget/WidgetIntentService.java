package barqsoft.footballscores.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;
import barqsoft.footballscores.scoresAdapter;

/**
 * Created by Ali on 08/08/15.
 */
public class WidgetIntentService extends IntentService {

    public WidgetIntentService() {
        super("WidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //Getting Today's data from the DB
        Cursor data = getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate(),
                null, null, new String[]{Utilies.getFormattedStringForTodayDate()}, null);
        if (data == null) return;
        if (!data.moveToFirst()) {
            data.close();
            return;
        }

        String home = data.getString(scoresAdapter.COL_HOME);
        String away = data.getString(scoresAdapter.COL_AWAY);
        String score = Utilies.getScores(data.getInt(scoresAdapter.COL_HOME_GOALS),
                data.getInt(scoresAdapter.COL_AWAY_GOALS));

        //Getting the widgets and updating their remote views with the data.
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, ScoreWidgetProvider.class));
        for (int appWidgetId : appWidgetIds) {

            RemoteViews rViews = new RemoteViews(getPackageName(), R.layout.widget_scores);
            rViews.setTextViewText(R.id.widget_home_name, home);
            rViews.setTextViewText(R.id.widget_away_name, away);
            rViews.setTextViewText(R.id.widget_score, score);
            rViews.setImageViewResource(R.id.widget_home_icon, Utilies.getTeamCrestByTeamName(home));
            rViews.setImageViewResource(R.id.widget_away_icon, Utilies.getTeamCrestByTeamName(away));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                rViews.setContentDescription(R.id.widget_home_name, home);
                rViews.setContentDescription(R.id.widget_away_name, away);
                rViews.setContentDescription(R.id.widget_score, score);
                rViews.setContentDescription(R.id.widget_home_icon, "Home Crest");
                rViews.setContentDescription(R.id.widget_away_icon, "Away Crest");
            }

            // Pending Intent to lunch the app from the widget
            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            rViews.setOnClickPendingIntent(R.id.widget, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, rViews);
        }
    }
}
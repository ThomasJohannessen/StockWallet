package no.stockwallet.Service;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import no.stockwallet.ViewModels.StockViewModel;

public class UpdateInvestmentsWorker extends Worker {

    public UpdateInvestmentsWorker(
        @NonNull Context context,
        @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        StockViewModel model = new StockViewModel();
        model.updateModel();
        Log.i("WorkManager","Worker running. Updating Investments");
        return Result.success();
    }


}

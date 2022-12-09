/**
 * This is Programming assignment 2 of CS 643 - Cloud computing.
 * A wine quality prediction ML model in Spark over AWS.
 * For this project I have used AWS EMR cluster that has one master EC2 and three Slave EC2 Vms.
 * 
 * @author Milankumar Patel (mkp6) 
 **/

package winequalityprediction;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class TestPrediction {
	
	/*
     * Main function for PredictionModelTrainer
     */
    public static void main(String[] args) {
    	
    	/*
    	 * Most logging operations, e xcept configuration, are done through this Logger Interface
    	 */
        Logger.getLogger("org").setLevel(Level.ERROR);
        
        /*
         * Check for the required arguments for the Model
         */
        if (args.length < 2) {
            System.err.println("Required args missing. {1} = Test file, {2} = model path");
            System.exit(1);
        }
        
        /*
    	 * Initialization of objects and get the parameters
    	 */
        final String validationDataSet = args[0];
        final String predictModelPath = args[1];
        
        /*
         * Sets a name for the application,if there is no existing one, creates a new one
         */
        SparkSession sparkSession = new SparkSession.Builder().appName("cs643 - Wine quality prediction").getOrCreate();
        
        /*
         * PredictionModelTrainer class object load, clean and transform dataset
         */
        PredictionModelTrainer predictionModelTrainer = new PredictionModelTrainer();

        /*
         * Reads an ML Model from the input path
         */
        PipelineModel model = PipelineModel.load(predictModelPath);
        
        /*
         * loads the data from csv and removes all dupliactes values from it
         */
        Dataset<Row> loadDataSet = predictionModelTrainer.loadDataset(sparkSession, validationDataSet);
        
        /*
         * this funcation will transforms the cleaned dataset into Vector
         */
        Dataset<Row> trainingFinalDataSet = predictionModelTrainer.transformDataset(loadDataSet);

        /*
         * Make predictions on test csv.
         */
        Dataset<Row> predictions = model.transform(testDataSet);
        predictions.show();

        /*
         * Evaluate Model performance
         */
        predictionModelTrainer.getPerformance(predictions);
    }
}
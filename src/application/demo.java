package application;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.bluetooth.RemoteDevice;

import Bitalino.Bitalino;
import Bitalino.BitalinoException;
import Bitalino.Frame;
import javafx.application.Application;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;

public class demo extends Application {
	
	public static Frame[] frame = null;
	@SuppressWarnings("rawtypes")
	private Series dataECG = new XYChart.Series();
	@SuppressWarnings("rawtypes")
	private Series dataEDA = new XYChart.Series();
	//private LineChart<String, String> ecgGraph;
	//private LineChart<String, String> edaGraph;
	private LineChart<Number, Number> ecgGraph;
	private LineChart<Number, Number> edaGraph;
	
	private Float x_axis;
	private Integer medianValue=500;
	

	@SuppressWarnings({ "rawtypes", "unchecked", "removal", "null" })
	@Override
	public void start(Stage stage)  {
		 Bitalino bitalino = null;
	        try {
	        	
	        	final NumberAxis xAxis = new NumberAxis();
	            final NumberAxis yAxis = new NumberAxis();
	            //creating the chart
	            ecgGraph = new LineChart<Number,Number>(xAxis, yAxis);
	            edaGraph = new LineChart<Number,Number>(xAxis,yAxis);
	            /*
	            final CategoryAxis xAxis = new CategoryAxis();
	            final CategoryAxis yAxis = new CategoryAxis();
	            ecgGraph = new LineChart<String, String>(xAxis, yAxis);
	            edaGraph = new LineChart<String,String>(xAxis,yAxis);
	            */
	        	
	            bitalino = new Bitalino();
	            // Code to find Devices
	            //Only works on some OS
	            Vector<RemoteDevice> devices = bitalino.findDevices();
	            System.out.println(devices);

	            //You need TO CHANGE THE MAC ADDRESS
	            //You should have the MAC ADDRESS in a sticker in the Bitalino
	            String macAddress = "20:17:11:20:50:75";
	            
	            //Sampling rate, should be 10, 100 or 1000
	            Integer SamplingRate = 1000;
	            bitalino.open(macAddress, SamplingRate);

	            // Start acquisition on analog channels A2 and A6
	            // For example, If you want A1, A3 and A4 you should use {0,2,3}
	            //int[] channelsToAcquire = {1, 2};
	            int[] channelsToAcquire = {1};

	            bitalino.start(channelsToAcquire);
	            
	            DecimalFormat df = new DecimalFormat("##.##");
	            
	            ecgGraph.getData().clear();
	            edaGraph.getData().clear();
	            
	            dataECG=new XYChart.Series();
	            dataEDA=new XYChart.Series();
	            
	            //Read in total 10000000 times
	            for (int j = 0; j < 1000; j++) {

	                //Each time read a block of 10 samples 
	                int block_size=10;
	                frame = bitalino.read(block_size);
	                
	                
	                
	                
	                //Print the samples
	                for (int i = 0; i < frame.length; i++) {
	                	// To get a JavaFX LineChart component to display any lines, you must provide it with a data series.
	                	// A data series is a list of data points. Each data point contains an X value and a Y value.
	                	
	                	x_axis= new Float((j * block_size + i + 0.16667)/SamplingRate);

	                	//dataECG.getData().add(new XYChart.Data(String.valueOf(x_axis.floatValue()), String.valueOf(frame[i].analog[0] )));
	                	dataECG.getData().add(new XYChart.Data(x_axis.floatValue(), frame[i].analog[0]-medianValue));

	                	//System.out.println(String.valueOf(df.format(x_axis.floatValue())) + " value:" + String.valueOf(frame[i].analog[0] -medianValue)+ " "+ dataECG.getData().toString());
	                	
	                	//dataEDA.getData().add(new XYChart.Data(String.valueOf(x_axis.floatValue()), String.valueOf(frame[i].analog[1])));
	                	
	                	
	                }
	                
	            }
	            
	            
	            //stop acquisition
	            bitalino.stop();
	            
	            ecgGraph.getData().addAll(dataECG);
	            //edaGraph.getData().addAll(dataEDA);
	            
	            //FlowPane pane = new FlowPane(ecgGraph,edaGraph);
	            
	            //Scene scene  = new Scene(pane,1000,600);
	            Scene scene  = new Scene(ecgGraph,1200,400);
                stage.setScene(scene);
                stage.show();
	            
	            
	            
	            
	        } catch (BitalinoException ex) {
	            Logger.getLogger(demo.class.getName()).log(Level.SEVERE, null, ex);
	        } catch (Throwable ex) {
	            Logger.getLogger(demo.class.getName()).log(Level.SEVERE, null, ex);
	        } finally {
	            try {
	                //close bluetooth connection
	                if (bitalino != null) {
	                    bitalino.close();
	                }
	            } catch (BitalinoException ex) {
	                Logger.getLogger(demo.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
		
	}
	

	public static void main(String[] args) {
		launch(args);
	}
	

}

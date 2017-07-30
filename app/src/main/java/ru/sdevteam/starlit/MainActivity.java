package ru.sdevteam.starlit;

import android.content.res.Resources;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import ru.sdevteam.starlit.craft.buildings.BuildingsRegistry;
import ru.sdevteam.starlit.utils.Drawing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.json.*;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		makeFullScreen();

		Drawing.initFonts(getAssets());

		try
		{
			String buildingsJsonStr = loadFileContent("buildings");
			JSONArray buildings = new JSONArray(buildingsJsonStr);
			BuildingsRegistry.registerFromJSON(buildings);
		}
		catch (IOException ex)
		{
			System.err.println("Reading error: " + ex.getMessage());
			System.exit(1);
		}
		catch (JSONException ex)
		{
			System.err.println("JSON error: " + ex.getMessage());
			System.exit(1);
		}
		catch (Exception ex)
		{
			System.err.println("Reflective error: " + ex.getMessage());
			System.exit(1);
		}

		setContentView(R.layout.activity_main);

		System.out.println("Activity onCreate");
	}

	private void makeFullScreen()
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
		{
			actionBar.hide();
		}
	}

	public String loadFileContent(String fileName) throws IOException
	{
		//Create a InputStream to read the file into
		Resources resources = getResources();
		InputStream iS;

		//get the resource id from the file name
		int rID = resources.getIdentifier("ru.sdevteam.starlit:raw/"+fileName, null, null);
		//get the file as a stream
		iS = resources.openRawResource(rID);

		//create a buffer that has the same size as the InputStream
		byte[] buffer = new byte[iS.available()];
		//read the text file as a stream, into the buffer
		iS.read(buffer);
		//create a output stream to write the buffer into
		ByteArrayOutputStream oS = new ByteArrayOutputStream();
		//write this buffer to the output stream
		oS.write(buffer);
		//Close the Input and Output streams
		oS.close();
		iS.close();

		//return the output stream as a String
		return oS.toString();
	}
}

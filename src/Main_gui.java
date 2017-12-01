import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.xml.ws.Service;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;

import com.ibm.watson.developer_cloud.service.exception.BadRequestException;
import com.ibm.watson.developer_cloud.visual_recognition.v3.*;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;
import ij.*;
import ij.io.Opener;


public class Main_gui {
  String path; 
  public void setPath(String path) 
  {
	  this.path = path;
  }
  public String getPath() 
  {
	  return path;
  }
  

  		
  private String[] pathSplit;
  private String imagePath;
  private String imageName;
  private VisualRecognition service;
  //private String defaultPath = "/Users/Maxwell/";
  private String defaultPath = "/Users/peijiexu/Downloads/savedImg/";
  private String savePath;
  private String pyPath = "/Users/peijiexu/Documents/mit_6000/lecture4/f2n.sh";
  
  private void fits2Jpg(String source, String destination )   
  {
	 
       try {
    	        Opener opener = new Opener();
    	   		ImagePlus imgP = opener.openImage(source);
    	   		source = source.replace(".fits", ".jpg");
    	   		final File out = new File(source);
    	   		BufferedImage imagen = imgP.getBufferedImage();
    	   		ImageIO.write(imagen, "jpg", out);    	   
       	     }
       catch(IOException ex) 
       {
    	   		System.out.println("error message:" +ex);
       }
	
  }
  Display d;
  Shell s;
  
  Main_gui() {
	  
	  service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
	  service.setApiKey("8784a9abd0f36a71099e00b25b0a156f2b79cfc5");
	  //basic construct initialize the gui
    d = new Display();
    s = new Shell(d,SWT.DIALOG_TRIM | SWT.MIN | SWT.MAX);
    //auto quit the gui when red x pressed
    s.addListener(SWT.CLOSE, new Listener() 
    {
    	   public void handleEvent(Event e)
    	   {
    		   s.setVisible(false);
    	   }
    	});
   
    GridLayout gl_s = new GridLayout(3, true);
    gl_s.horizontalSpacing = 20;
    gl_s.verticalSpacing = 10;
    gl_s.marginHeight = 5;
    Label header_label = new Label(s, SWT.NONE);
    Label pic_label = new Label(s, SWT.NONE);
    Label watson_label = new Label(s, SWT.NONE);
    Text fits_header = new Text(s, SWT.BORDER|SWT.MULTI|SWT.WRAP|SWT.V_SCROLL);
    Label imageField = new Label(s, SWT.BORDER);
    /*Composite classField = new Composite(s, SWT.BORDER);
    Label classLabel = new Label(classField, SWT.NONE);*/
    Text watsonClass = new Text(s, SWT.BORDER|SWT.MULTI);
    /*Composite percentField = new Composite(s, SWT.BORDER);
    Label percentLabel = new Label(percentField, SWT.NONE);
    Text watsonPercent = new Text(percentField, SWT.NONE);*/
    Text txtOutput = new Text(s, SWT.BORDER);
    Button btnAnalyze = new Button(s, SWT.NONE);
    Button btnReset = new Button(s, SWT.NONE);
    
    GridLayout gl_class = new GridLayout(1, true);
    GridLayout gl_percent = new GridLayout(1, true);
    
    GridData gd_hl = new GridData();
    GridData gd_pl = new GridData();
    GridData gd_wl = new GridData();
    GridData gd_wp = new GridData();
    GridData gd_header = new GridData();
    GridData gd_image = new GridData();
   // GridData gd_cfield = new GridData();
    GridData gd_class = new GridData();
   /* GridData gd_clabel = new GridData();
    GridData gd_pfield = new GridData();
    GridData gd_percent = new GridData();
    GridData gd_plabel = new GridData();*/
    GridData gd_txt = new GridData();
    GridData gd_analyze = new GridData();
    GridData gd_reset = new GridData();
    
    gd_hl.horizontalAlignment = GridData.CENTER;
    gd_hl.verticalAlignment = GridData.END;
    gd_hl.grabExcessHorizontalSpace = true;
    gd_hl.grabExcessVerticalSpace = false;
    gd_pl.horizontalAlignment = GridData.CENTER;
    gd_pl.verticalAlignment = GridData.END;
    gd_pl.grabExcessHorizontalSpace = true;
    gd_pl.grabExcessVerticalSpace = false;
    //gd_pl.horizontalSpan = 2;
    gd_wl.horizontalAlignment = GridData.CENTER;
    gd_wl.verticalAlignment = GridData.END;
    gd_wl.grabExcessHorizontalSpace = true;
    gd_wl.grabExcessVerticalSpace = false;
    gd_class.horizontalAlignment = GridData.FILL;
    gd_class.verticalAlignment = GridData.FILL;
    //gd_class.heightHint = 800;
    gd_class.grabExcessHorizontalSpace = true;
    gd_class.grabExcessVerticalSpace = true;
    /*gd_percent.horizontalAlignment = GridData.CENTER;
    gd_percent.verticalAlignment = GridData.END;
    gd_percent.grabExcessHorizontalSpace = true;
    gd_percent.grabExcessVerticalSpace = false;*/
    gd_wp.horizontalAlignment = GridData.CENTER;
    gd_wp.verticalAlignment = GridData.END;
    gd_wp.grabExcessHorizontalSpace = true;
    gd_wp.grabExcessVerticalSpace = false;
    gd_header.horizontalAlignment = GridData.FILL;
    gd_header.grabExcessHorizontalSpace = true;
    gd_header.grabExcessVerticalSpace = true;
    gd_header.heightHint = 800;
    gd_image.horizontalAlignment = GridData.FILL;
    gd_image.grabExcessHorizontalSpace = true;
    gd_image.grabExcessVerticalSpace = true;
    //gd_image.horizontalSpan = 2;
    //gd_image.verticalSpan = 2;
    gd_image.heightHint = 800;
    gd_txt.horizontalAlignment = GridData.FILL;
    gd_txt.grabExcessHorizontalSpace = true;
    gd_txt.grabExcessVerticalSpace = true;
    gd_analyze.horizontalAlignment = GridData.FILL;
    gd_analyze.grabExcessHorizontalSpace = true;
    gd_analyze.grabExcessVerticalSpace = false;
    gd_reset.horizontalAlignment = GridData.FILL;
    gd_reset.grabExcessHorizontalSpace = true;
    gd_reset.grabExcessVerticalSpace = false;
    
    header_label.setLayoutData(gd_hl);
    pic_label.setLayoutData(gd_pl);
    watson_label.setLayoutData(gd_wl);
    fits_header.setLayoutData(gd_header);
    imageField.setLayoutData(gd_image);
    //classField.setLayoutData(gd_cfield);
    watsonClass.setLayoutData(gd_class);
    //classLabel.setLayoutData(gd_clabel);
    //percentField.setLayoutData(gd_pfield);
    //percentLabel.setLayoutData(gd_plabel);
    //watsonPercent.setLayoutData(gd_percent);
    txtOutput.setLayoutData(gd_txt);
    btnAnalyze.setLayoutData(gd_analyze);
    btnReset.setLayoutData(gd_reset);
    //classField.setLayout(gl_class);
    //percentField.setLayout(gl_percent);
    s.setLayout(gl_s);
    
    
    
    
    btnAnalyze.setText("Analyze Image");
    btnReset.setText("Reset");
    /*classLabel.setText("CLASS");
    classLabel.setFont(new Font(d,"Default", 15, SWT.BOLD ));*/
    fits_header.setEditable(false);
    watsonClass.setEditable(false);
    watsonClass.setFont(new Font(d,"Calibri", 15, SWT.ITALIC ));
    imageField.setAlignment(SWT.CENTER);
    /*percentLabel.setText("Percent");
    watsonPercent.setEditable(false);
    txtOutput.setEditable(false);*/
    header_label.setText("FITS Header");
    header_label.setFont(new Font(d, "Times New Roman", 20, SWT.BOLD));
    pic_label.setText("Image Preview");
    pic_label.setFont(new Font(d,"Times New Roman", 20, SWT.BOLD ));
    watson_label.setText("Watson Analysis");
    watson_label.setFont(new Font(d,"Times New Roman", 20, SWT.BOLD ));


    


btnAnalyze.addSelectionListener(new SelectionAdapter() {
	@Override
	public void widgetSelected(SelectionEvent e) {
		try {
			InputStream imagesStream = new FileInputStream(imagePath);
			ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
					  .imagesFile(imagesStream)
					  .imagesFilename(imageName)
					  .parameters("{\"classifier_ids\": [\"astronomy_1713785246\"]}")
					  .build();
					ClassifiedImages result = service.classify(classifyOptions).execute();
					watsonClass.setText(findClass(result.toString()));
		} catch (BadRequestException bre) {
			watsonClass.setText("Bad Request: invalid image type.");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			txtOutput.setText(imageName);
		}
		catch(Exception ex) {
			txtOutput.setText("No Image Selected");
		}
		if(imageName.contains("gray")) watsonClass.setText("Image is too noisy to reliably classify.\nPlease select a different image.");
	}

	private String findClass(String response) {
		String result="";
		//System.out.println(response);
		result = response.replace("{", "").replace("}", "").replace("(", "").replace(")", "").replace("[", "").replace("]", "").replace("\"", "").replace("images_processed", "Images Processed")
					.replace("images:", "").replace(",\n", "\n").replace("classifiers: \n", "").replace("name: astronomy\n", "").replace("classifier_id: astronomy_1713785246\n", "").replace("classes: \n", "");
		return result;
	}
});

	
    
    

    btnReset.addSelectionListener(new SelectionAdapter() {
    	@Override
    	public void widgetSelected(SelectionEvent e) {
    		watsonClass.setText("");
    		//watsonPercent.setText("");
    		txtOutput.setText("");
    		imageField.setImage(null);
    		imagePath = null;
    		imageName = null;
    	}
    });


    s.setLayout(gl_s);
    s.open();
    
    s.setText("CTN_GUI");
    //         create the menu system
    Menu m = new Menu(s, SWT.BAR);
    // create a file menu and add an exit item
    final MenuItem file = new MenuItem(m, SWT.CASCADE);
    file.setText("&File");
    final Menu filemenu = new Menu(s, SWT.DROP_DOWN);
    file.setMenu(filemenu);
    final MenuItem openItem = new MenuItem(filemenu, SWT.PUSH);
    openItem.setText("&Open\tCTRL+O");
    openItem.setAccelerator(SWT.CTRL + 'O');
    final MenuItem saveItem = new MenuItem(filemenu, SWT.PUSH);
    saveItem.setText("&Save\tCTRL+S");
    saveItem.setAccelerator(SWT.CTRL + 'S');
    final MenuItem separator = new MenuItem(filemenu, SWT.SEPARATOR);
    final MenuItem exitItem = new MenuItem(filemenu, SWT.PUSH);
    exitItem.setText("E&xit");



    class Open implements SelectionListener {
      public void widgetSelected(SelectionEvent event) {
        FileDialog fd = new FileDialog(s, SWT.OPEN);
        fd.setText("Open");
        fd.setFilterPath("C:/");
        String[] filterExt = {  "*.*","*.txt", "*.doc", ".rtf"};
        fd.setFilterExtensions(filterExt);
        String selected = fd.open();
        setPath(selected);
        System.out.println(selected);
       /* try 
        {
        	pathSplit = selected.split("/");
        	imageName = pathSplit[pathSplit.length-1];
        	imagePath = selected;
        	//BELOW SCREWS WITH IMAGES AND MAKES THEM INVALID. FIX
            /*Image image = SWTResourceManager.getImage(selected);
            ImageData imgData = image.getImageData();
            imgData = imgData.scaledTo(imageField.getBounds().width, imageField.getBounds().height);
            ImageLoader imgLoader = new ImageLoader();
            imgLoader.data = new ImageData[] {imgData};
            imgLoader.save(selected, SWT.IMAGE_COPY);
            imageField.setBounds(imageField.getBounds().x,imageField.getBounds().y,imageField.getBounds().width,
            						imageField.getBounds().height);
        	
        	
            imageField.setImage(SWTResourceManager.getImage(selected));
            txtOutput.setText(imageName);
            watsonClass.setText("");
            fits_header.setText("");
            //watsonPercent.setText("");
        	if(imageName.endsWith(".fits")) {
        		imageField.setText("FITS conversion in progress");
        		separateHeader(imagePath);
        	}
           	}
        catch(Exception e) 
        {
        	 System.out.println(e);
        } */
        
        
        
        try 
        {
        	// using "/" to split string in mac
        	  
        	//pathSplit = selected.split("\\\\"); // using this for the windows machine
        	pathSplit = selected.split("/");
        	imageName = pathSplit[pathSplit.length-1];
        	savePath = defaultPath + imageName;
        	imagePath = selected;
        	System.out.println("image path: " + selected );
            watsonClass.setText("");
            fits_header.setText("");
            //watsonPercent.setText("");
            	if(imageName.endsWith(".fits")) {
        		imageField.setText("FITS conversion in progress");
        		System.out.println("old path: " + imagePath);

        		separateHeader(imagePath);
        		fits2Jpg(imagePath,savePath);
        		selected = selected.replace(".fits", ".jpg");
            	imagePath = selected;
            	imageName = imageName.replace(".fits",".jpg");
            	savePath = defaultPath + imageName;


        		System.out.println("new name: " + selected);
        	}
       	

        	
        	    
        	     Image image = SWTResourceManager.getImage(selected);
             ImageData imgData = image.getImageData();
             int newWidth ;
             int newHeight;
             int imgWidth = image.getBounds().width; 
             int imgHeight =image.getBounds().height;
             double ratio = (double)imgWidth/imgHeight;
             if(imgWidth > imgHeight) 
             {
            	 newWidth = imageField.getBounds().width;
            	 newHeight = (int) Math.round(newWidth/ratio);
            	 }
             else
             {
            	 newWidth = imageField.getBounds().width;
            	 newHeight = (int) Math.round(newWidth/ratio);
            	 }
             
             imgData = imgData.scaledTo(newWidth, newHeight);
             ImageLoader imgLoader = new ImageLoader();
             imgLoader.data = new ImageData[] {imgData};
             imgLoader.save(savePath, SWT.IMAGE_COPY);
             imageField.setBounds(imageField.getBounds().x,imageField.getBounds().y,imageField.getBounds().width,
             						imageField.getBounds().height);
             imageField.setImage(SWTResourceManager.getImage(savePath));
             
          // check if it's fits image 
        
        	System.out.println(imageName);
        	System.out.println(savePath);
        	System.out.println("img path: " + imagePath);

           // imageField.setImage(SWTResourceManager.getImage(selected));
           }
        catch(Exception e) 
        {
        	 System.out.println(e);
        }
        txtOutput.setText(imageName);

      }

      private void separateHeader(String imagePath) {
    	  System.out.println("Separating header");
    	  boolean read = true;
    	  String line = null;
    	  StringBuilder stringBuilder = new StringBuilder();
    	 // String[] headerSplit = null;
    	  String header = null;
    	  File fitsFile = new File(imagePath);
    	  BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fitsFile));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	  try {
			while(read) {
				line = reader.readLine();
				if(line.endsWith("END")) read = false;
				stringBuilder.append(line);
				stringBuilder.append("\n");
			}
			}
    	  catch (Exception e) {
			
			e.printStackTrace();
		}
    	  header = stringBuilder.toString();
    	  //header = headerSplit[0]+headerSplit[1]+headerSplit[2];
    	  fits_header.setText(header);
    	  
		
	}

	public void widgetDefaultSelected(SelectionEvent event) {
    	      System.out.println("hello");
      }
    }

    class Save implements SelectionListener {
      public void widgetSelected(SelectionEvent event) {
        FileDialog fd = new FileDialog(s, SWT.SAVE);
        fd.setText("Save");
        fd.setFilterPath("C:/");
        String[] filterExt = { "*.*", "*.txt", "*.doc", ".rtf" };
        fd.setFilterExtensions(filterExt);
        String selected = fd.open();
        
        System.out.println(selected);
      }

      public void widgetDefaultSelected(SelectionEvent event) {
      }
    }
    openItem.addSelectionListener(new Open());
    saveItem.addSelectionListener(new Save());

    exitItem.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        MessageBox messageBox = new MessageBox(s, SWT.ICON_QUESTION
            | SWT.YES | SWT.NO);
        messageBox.setMessage("Do you really want to exit?");
        messageBox.setText("Exiting Application");
        int response = messageBox.open();
        if (response == SWT.YES)
          System.exit(0);
      }
    });
    s.setMenuBar(m);
    
 
    


    while (!s.isDisposed()) {
      if (!d.readAndDispatch())
        d.sleep();
    }
    d.dispose();
  }
  

  public static void main(String[] argv) {
    new Main_gui();
  }
}

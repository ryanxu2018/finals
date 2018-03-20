
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.crypto.Mac;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.layout.GridData;

import org.eclipse.swt.widgets.TreeItem;

import com.ibm.watson.developer_cloud.service.exception.BadRequestException;
import com.ibm.watson.developer_cloud.visual_recognition.v3.*;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.Classifier;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.UpdateClassifierOptions;



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
  
  //parameter for gui default
  private String[] pathSplit;
  private String imagePath;
  private String imageName;
  private String updClassifier;
  private String creClassifier;
  private VisualRecognition service;
  private String defaultPath = "/Users/peijiexu/Downloads/savedImg/";
  private String savePath;
  private String classfiersId = "astronomy_1713785246";
  private String[] classes;


  Display d;
  Shell s;
  
  
  
  Main_gui() {
	  //setting up watson vr api service
	  service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
	  service.setApiKey("8784a9abd0f36a71099e00b25b0a156f2b79cfc5");
	 // ListClassifiersOptions listClassifiersOptions = new ListClassifiersOptions.Builder().verbose(true).build();
	  //Classifiers classifiers = service.listClassifiers(listClassifiersOptions).execute();
	  //System.out.println(classifiers);
	  
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
    Tree tree = new Tree(s, SWT.BORDER);
    

    Label imageField = new Label(s, SWT.BORDER);
   
    TabFolder tabFolder = new TabFolder(s,SWT.BORDER);
    TabItem one = new TabItem(tabFolder, SWT.NULL);
    one.setText("Results");
    Text ones = new Text (tabFolder, SWT.NULL|SWT.H_SCROLL|SWT.V_SCROLL);
    ones.setEditable(false);
    one.setControl(ones);
    
    TabItem two = new TabItem(tabFolder, SWT.NULL);
    two.setText("FITS Header");
    Text twos  = new Text (tabFolder, SWT.NULL|SWT.H_SCROLL|SWT.V_SCROLL);
    twos.setEditable(false);
    two.setControl(twos);
    
    TabItem three = new TabItem(tabFolder, SWT.NULL|SWT.H_SCROLL|SWT.V_SCROLL);
    three.setText("ERROR");
    Text threes  = new Text (tabFolder, SWT.NULL);
    threes.setEditable(false);
    three.setControl(threes);
    
    //Text txtOutput = new Text(s, SWT.BORDER);
    Button trainWatson = new Button(s, SWT.NONE);

    Button btnAnalyze = new Button(s, SWT.NONE);
    Button btnReset = new Button(s, SWT.NONE);
    
 
    
    GridData gd_hl = new GridData();
    GridData gd_pl = new GridData();
    GridData gd_wl = new GridData();
    GridData gd_wp = new GridData();
    GridData gd_header = new GridData();
    GridData gd_image = new GridData();
    GridData gd_class = new GridData();
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
    gd_wl.horizontalAlignment = GridData.CENTER;
    gd_wl.verticalAlignment = GridData.END;
    gd_wl.grabExcessHorizontalSpace = true;
    gd_wl.grabExcessVerticalSpace = false;
    gd_class.horizontalAlignment = GridData.FILL;
    gd_class.verticalAlignment = GridData.FILL;
    gd_class.grabExcessHorizontalSpace = true;
    gd_class.grabExcessVerticalSpace = true;
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
    
    imageField.setLayoutData(gd_image);
    tabFolder.setLayoutData(gd_class);
 
    trainWatson.setLayoutData(gd_txt);
    ones.setLayoutData(gd_txt);
    twos.setLayoutData(gd_txt);

    btnAnalyze.setLayoutData(gd_analyze);
    btnReset.setLayoutData(gd_reset);

    s.setLayout(gl_s);
    trainWatson.setText("Train Watson");
    btnAnalyze.setText("Analyze Image");
    btnReset.setText("Reset");

 
    ones.setFont(new Font(d,"Calibri", 15, SWT.ITALIC ));
    imageField.setAlignment(SWT.CENTER);
   
    header_label.setText("File Explorer");
    header_label.setFont(new Font(d, "Times New Roman", 20, SWT.BOLD));
    tree.setLayoutData(gd_header);
    pic_label.setText("Image Preview");
    pic_label.setFont(new Font(d,"Times New Roman", 20, SWT.BOLD ));
    watson_label.setText("Watson Analysis");
    watson_label.setFont(new Font(d,"Times New Roman", 20, SWT.BOLD ));
    File base_dir = new File("/Users/peijiexu/");
    File [] roots = base_dir.listFiles ();
    for (int i=0; i<roots.length; i++) {
 	 // only displayed the roots we need
     if(roots[i].toString().contains("Downloads")) {
     TreeItem root = new TreeItem (tree, 0);
     String [] new_str = roots[i].toString().split("/");
     String display_str = new_str[new_str.length -1];
     root.setText (display_str);
     root.setData (roots [i]);
    
     System.out.println(roots[i]);
     new TreeItem (root, 0);
     }
   }
   tree.addListener (SWT.Expand, new Listener () {
	    public void handleEvent (final Event event) {
	    	 
	      final TreeItem rootss = (TreeItem) event.item;
	      TreeItem [] items = rootss.getItems ();
	      for (int i= 0; i<items.length; i++) {
	    	    System.out.println("current item" +items[i]);
	       
	        items [i].dispose ();
	      }
	      File file = (File) rootss.getData ();
	      File [] files = file.listFiles ();
	      if (files == null) return;
	      for (int i= 0; i<files.length; i++) {
	        TreeItem item = new TreeItem (rootss, 0);
	        item.setText (files [i].getName ());
	       
	        item.setData (files [i]);
	        if (files [i].isDirectory()) {
	          new TreeItem (item, 0);
	        }
	      }
	    }
	  });
   
   tree.addListener (SWT.MouseDoubleClick, new Listener () {
	    public void handleEvent (final Event event) {
	    	Point points = new Point(event.x,event.y);
	    	TreeItem item = tree.getItem(points);
	    System.out.println(item);
	    try {
	    if(item.toString().contains(".png") || item.toString().contains(".jpg") || item.toString().contains(".zip"))
	    {
	        try 
	        {
		    	File file = (File) item.getData();
		    	if (file.isDirectory()) 
		    	{
		    		creClassifier = file.toString();

		    		return;
		 	}
		    	else if (file.toString().contains(".zip"))
		    	{
		    		updClassifier = file.toString();
		    		return;
		    		
		    	}

	        	imagePath = file.toString();
	        	pathSplit = imagePath.split("/");
	        	imageName = pathSplit[pathSplit.length-1];
	        	savePath = defaultPath + imageName;
	           ones.setText("");
	           one.setControl(ones);
	            	Image image = SWTResourceManager.getImage(imagePath);
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
	             
	    

	           }
	        catch(Exception e) 
	        {
	        	 System.out.println(e);
	        }
	     
	       }
	    else if(item.toString().contains(".fits")) 
	    {
	    	  twos.setText("");
	    	  twos.setText("FITS Header coming soon!");
	    	  return;
	    	
	    }
	    else 
	    {
	    	threes.setText("");
	    	threes.setText("Invalid type");
	    }}
	    catch(NullPointerException ex3)
	    {
	    	
	    	System.out.println(ex3);
	    }	    
	    }
	    
	  });

    


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
					ones.setText("");
					ones.setText(findClass(result.toString()));
		} catch (BadRequestException bre) {
			threes.setText("");
			threes.setText("Bad Request: invalid image type.");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch(NullPointerException e3) 
		{
			e3.printStackTrace();
			
			
		}
	
	}

	private String findClass(String response) {
		String result="";
		result = response.replace("{", "").replace("}", "").replace("(", "").replace(")", "").replace("[", "").replace("]", "").replace("\"", "").replace("images_processed", "Images Processed")
					.replace("images:", "").replace(",\n", "\n").replace("classifiers: \n", "").replace("name: astronomy\n", "").replace("classifier_id: astronomy_1713785246\n", "").replace("classes: \n", "");
		return result;
	}
});
trainWatson.addSelectionListener(new SelectionAdapter() {
	@Override
	public void widgetSelected(SelectionEvent e) {
		String name;
		if (updClassifier !=null) 
		{
			classes = updClassifier.toString().split("/");
			System.out.println(updClassifier.toString());
			System.out.println(classes[classes.length-1]);
			name = classes[classes.length-1];
			
			name = name.replace(".zip", "");
			try {
			UpdateClassifierOptions options = new UpdateClassifierOptions.Builder().classifierId(classfiersId).addClass(name,new File(updClassifier.toString())).build();
			Classifier updates = service.updateClassifier(options).execute();
			threes.setText(updates.toString());}
			catch(Exception ex) {
			threes.setText(ex.toString());
			}

			
		}
		else
			System.out.println("not valid");
	}
});
	
    
    

    btnReset.addSelectionListener(new SelectionAdapter() {
    	@Override
    	public void widgetSelected(SelectionEvent e) {
    		
    		
    		imageField.setImage(null);
    		imagePath = null;
    		imageName = null;
    		creClassifier = null;
    		updClassifier = null;
    		pathSplit = null;
    		classes = null;
    		ones.setText("");
    		twos.setText("");
    		threes.setText("");
    		
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
   // final MenuItem separator = new MenuItem(filemenu, SWT.SEPARATOR);
    final MenuItem exitItem = new MenuItem(filemenu, SWT.PUSH);
    exitItem.setText("E&xit");



    class Open implements SelectionListener {
      public void widgetSelected(SelectionEvent event) {
        FileDialog fd = new FileDialog(s, SWT.OPEN);
        fd.setText("Open");
        fd.setFilterPath("C:/");
        String[] filterExt = {  "*.*","*.txt", "*.doc", ".rtf", ""};
        fd.setFilterExtensions(filterExt);
        String selected = fd.open();
        setPath(selected);
        
        if(selected.contains(".jpg") || selected.contains("png")) {
        try 
        {
        	
     	imagePath = selected;
        	pathSplit = selected.split("/");
        	imageName = pathSplit[pathSplit.length-1];
        	savePath = defaultPath + imageName;
             ones.setText("");

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
             
    

           }
        catch(Exception e) 
        {
        	 System.out.println(e);
        }
      }
        else if(selected.contains(".fits"))
        {
        	ones.setText("");
        	ones.setText("FITS Header information coming soon!!");
        	one.setControl(ones);

        }
        else if (selected.contains(".zip")) 
        {
        	updClassifier = selected.toString();
        	
        }
      }

	public void widgetDefaultSelected(SelectionEvent event) {
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

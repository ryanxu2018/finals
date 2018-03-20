import java.io.FileInputStream;
import java.io.IOException;
 
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
  
public class Main {
   public static void main(String[] args) throws Exception {
      Display display = new Display();
      Shell shell = new Shell(display);
  
      // pos x, pos y, width, height
      shell.setBounds(200, 200, 300, 200);
      shell.setText("SWT Tree Demonstration");
      shell.setLayout(new GridLayout());
  
      Tree tree = new Tree(shell, SWT.BORDER);
        
      TreeItem node1 = new TreeItem (tree, SWT.NULL);
      node1.setText("My Computer");
      node1.setImage(getImage("mycomp.gif"));
        
      TreeItem node2 = new TreeItem(node1, SWT.NULL);
      node2.setText("Audio");
      node2.setImage(getImage("folder.gif"));
  
      TreeItem node3 = new TreeItem(node1, SWT.NULL);
      node3.setText("Control Panel");
      node3.setImage(getImage("folder.gif"));
  
      TreeItem node4 = new TreeItem(node1, SWT.NULL);
      node4.setText("Folder");
      node4.setImage(getImage("folder.gif"));
        
      TreeItem node5 = new TreeItem(node1, SWT.NULL);
      node5.setText("Local Disk (C:)");
      node5.setImage(getImage("folder.gif"));
  
      TreeItem node6 = new TreeItem(node1, SWT.NULL);
      node6.setText("doc on '192.168.0.1' (Z:)");
      node6.setImage(getImage("folder.gif"));
  
      tree.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
              
      shell.open();
  
      while (!shell.isDisposed()) {
         if (!display.readAndDispatch()) {
            display.sleep();
         }
      }
        
      display.dispose();
   }
     
   public static Image getImage(String location) throws IOException {
      ImageData source = new ImageData(Main.class.getResourceAsStream(location));
      ImageData mask = source.getTransparencyMask();
      return new Image(null, source, mask);  
   }          
}
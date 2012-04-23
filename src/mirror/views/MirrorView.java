package mirror.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;

public class MirrorView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "mirror.views.MirrorView";
	private StyledText text;

	private ITextListener
	
	private IResourceChangeListener listener = new IResourceChangeListener() {
		public void resourceChanged(IResourceChangeEvent event) {
			text.setText(event.toString());
		}
	};
	
	
	/**
	 * The constructor.
	 */
	public MirrorView() {
	}
	
	public String getCurrentEditorContent() {
	    final IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
	            .getActiveEditor();
	    if (activeEditor == null) {
	    	System.out.println("active editor null");
	        return null;
	    }
	    
	    activeEditor.getAdapter(null);
	    
	    final IDocument doc = (IDocument) activeEditor.getAdapter(IDocument.class);
	    if (doc == null) {
	    	System.out.println("document null");
	    	return null;
	    }
	    return doc.get();
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		text = new StyledText(parent, SWT.BORDER);
		text.setEditable(false);
		
		
		text.setText("Anders");
		
		ResourcesPlugin.getWorkspace().addResourceChangeListener(listener);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		text.setFocus();
		System.out.println("focus");
		text.setText(text.getText()+".");
	}

}
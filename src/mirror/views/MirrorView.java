package mirror.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class MirrorView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "mirror.views.MirrorView";
	private StyledText text;

	/**
	 * The constructor.
	 */
	public MirrorView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		text = new StyledText(parent, SWT.BORDER);
		text.setEditable(false);

		text.setText("Anders");
	}
		/**
		 * Passing the focus request to the viewer's control.
		 */
		public void setFocus() {
			text.setFocus();
			System.out.println("focus");
			text.setText(text.getText()+".");
		}

//		public void dispose() {
//			super.dispose();
//		}

	}
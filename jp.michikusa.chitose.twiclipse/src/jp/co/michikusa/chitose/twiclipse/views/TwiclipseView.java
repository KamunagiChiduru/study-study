package jp.co.michikusa.chitose.twiclipse.views;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be presented in the view. Each
 * view can present the same model objects using different labels and icons, if needed.
 * Alternatively, a single label provider can be shared between views in order to ensure that
 * objects of the same type are presented in the same way everywhere.
 * <p>
 */

public class TwiclipseView extends ViewPart
{
    /**
     * The ID of the view as specified by the extension.
     */
    public static final String ID= "jp.co.michikusa.chitose.twiclipse.views.TwiclipseView";

    private Button syncronizeButton;

    private TableViewer tweetsViewer;

    /**
     * This is a callback that will allow us
     * to create the viewer and initialize it.
     */
    @Override
    public void createPartControl(Composite parent)
    {
        // Create the help context id for the viewer's control
        // PlatformUI.getWorkbench().getHelpSystem().setHelp( //
        // viewer.getControl(), //
        // "jp.co.michikusa.chitose.twiclipse.viewer" //
        // );

        final ScrolledComposite base= new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);

        base.setLayout(GridLayoutFactory.fillDefaults().create());
        base.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
        base.setExpandVertical(true);
        base.setExpandHorizontal(true);

        final Composite content= this.newContent(base);

        content.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

        base.setContent(content);

        final Point point= content.computeSize(SWT.DEFAULT, SWT.DEFAULT);

        content.setSize(point);
        base.setMinSize(point);
    }

    private Composite newContent(Composite parent)
    {
        final Composite base= new Composite(parent, SWT.NONE);

        base.setLayout(GridLayoutFactory.fillDefaults().create());

        {
            this.syncronizeButton= new Button(base, SWT.PUSH);

            this.syncronizeButton.setText("syncronize");
        }
        {
            this.tweetsViewer= new TableViewer(base);

            this.tweetsViewer.getTable().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
            this.tweetsViewer.getTable().setHeaderVisible(true);
            this.tweetsViewer.getTable().setLinesVisible(true);

            TableViewerColumn column;

            column= new TableViewerColumn(this.tweetsViewer, SWT.LEFT);
            column.getColumn().setText("user name");
            column.getColumn().pack();

            column= new TableViewerColumn(this.tweetsViewer, SWT.LEFT);
            column.getColumn().setText("tweet");
            column.getColumn().pack();

            column= new TableViewerColumn(this.tweetsViewer, SWT.CENTER);
            column.getColumn().setText("posted at");
            column.getColumn().pack();
        }

        return base;
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus()
    {
        this.tweetsViewer.getControl().setFocus();
    }
}

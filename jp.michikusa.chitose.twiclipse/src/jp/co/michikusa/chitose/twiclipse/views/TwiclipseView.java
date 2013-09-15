package jp.co.michikusa.chitose.twiclipse.views;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

import com.google.common.base.Joiner;

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

public class TwiclipseView extends ViewPart implements SelectionListener
{
    /**
     * The ID of the view as specified by the extension.
     */
    public static final String ID= "jp.co.michikusa.chitose.twiclipse.views.SampleView";

    private Action action1;
    private Action action2;
    private Action doubleClickAction;

    @Override
    public void widgetDefaultSelected(SelectionEvent e)
    {
    }

    @Override
    public void widgetSelected(SelectionEvent event)
    {
        try
        {
            final String source= Joiner.on("\n").join( //
                    Files.readAllLines( //
                            new File("C:/Users/USER1/Documents/sources/java/src/jp/michikusa/chitose/Simple.java").toPath(), //
                            Charset.forName("UTF-8") //
                    ));
            System.out.println("<!-- ===== START ===== -->");

            final ASTParser parser= ASTParser.newParser(AST.JLS4);

            parser.setResolveBindings(true);
            parser.setSource(source.toCharArray());

            final ASTNode node= parser.createAST(null);

            node.accept(new ASTVisitor()
            {
                @Override
                public boolean visit(MethodDeclaration node)
                {
                    System.out.println("### MethodDeclaration: " + node);

                    node.accept(new ASTVisitor()
                    {
                        @Override
                        public boolean visit(VariableDeclarationStatement node)
                        {
                            System.out.println("### VariableDeclarationStatement: " + node);

                            return false;
                        }
                    });

                    return true;
                }
            });

            System.out.println("<!-- ===== END ===== -->");
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

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

        base.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        base.setExpandVertical(false);
        base.setExpandHorizontal(false);

        final Composite content= this.newContent(base);

        content.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

        base.setContent(content);

        final Point point= content.computeSize(SWT.DEFAULT, SWT.DEFAULT);

        content.setSize(point);
        base.setMinSize(point);

        // makeActions();
        // hookContextMenu();
        // hookDoubleClickAction();
        // contributeToActionBars();
    }

    private Composite newContent(Composite parent)
    {
        final Composite base= new Composite(parent, SWT.NONE);

        base.setLayout(GridLayoutFactory.swtDefaults().create());

        {
            final Button reflesh= new Button(base, SWT.PUSH);

            reflesh.setText("syncronize");
            reflesh.addSelectionListener(this);
        }
        {
            final TableViewer viewer= new TableViewer(base);

            final TableViewerColumn column= new TableViewerColumn(viewer, SWT.NONE);

            column.getColumn().setText("user");
        }

        return base;
    }

    private void hookContextMenu()
    {
        // final MenuManager menuMgr= new MenuManager("#PopupMenu");
        //
        // menuMgr.setRemoveAllWhenShown(true);
        // menuMgr.addMenuListener(new IMenuListener(){
        // @Override
        // public void menuAboutToShow(IMenuManager manager){
        // SampleView.this.fillContextMenu(manager);
        // }
        // });
        //
        // final Menu menu= menuMgr.createContextMenu(viewer.getControl());
        // viewer.getControl().setMenu(menu);
        // getSite().registerContextMenu(menuMgr, viewer);
    }

    private void contributeToActionBars()
    {
        IActionBars bars= getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());
    }

    private void fillLocalPullDown(IMenuManager manager)
    {
        manager.add(action1);
        manager.add(new Separator());
        manager.add(action2);
    }

    // private void fillContextMenu(IMenuManager manager){
    // manager.add(action1);
    // manager.add(action2);
    // // Other plug-ins can contribute there actions here
    // manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    // }

    private void fillLocalToolBar(IToolBarManager manager)
    {
        manager.add(action1);
        manager.add(action2);
    }

    // private void makeActions(){
    // action1= new Action(){
    // @Override
    // public void run(){
    // showMessage("Action 1 executed");
    // }
    // };
    // action1.setText("Action 1");
    // action1.setToolTipText("Action 1 tooltip");
    // action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
    // getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
    //
    // action2= new Action(){
    // @Override
    // public void run(){
    // showMessage("Action 2 executed");
    // }
    // };
    // action2.setText("Action 2");
    // action2.setToolTipText("Action 2 tooltip");
    // action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
    // getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
    // doubleClickAction= new Action(){
    // @Override
    // public void run(){
    // ISelection selection= viewer.getSelection();
    // Object obj= ((IStructuredSelection)selection).getFirstElement();
    // showMessage("Double-click detected on " + obj.toString());
    // }
    // };
    // }

    // private void hookDoubleClickAction(){
    // viewer.addDoubleClickListener(new IDoubleClickListener(){
    // @Override
    // public void doubleClick(DoubleClickEvent event){
    // doubleClickAction.run();
    // }
    // });
    // }

    // private void showMessage(String message){
    // MessageDialog.openInformation(
    // viewer.getControl().getShell(),
    // "Sample View",
    // message);
    // }

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus()
    {
        // viewer.getControl().setFocus();
    }
}

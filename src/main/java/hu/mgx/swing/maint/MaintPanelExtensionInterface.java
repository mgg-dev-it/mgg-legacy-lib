package hu.mgx.swing.maint;

import hu.mgx.db.TableDefinition;
import hu.mgx.swing.DataField;
import java.awt.Component;

public interface MaintPanelExtensionInterface
{

    public Component createFieldComponent(TableDefinition td, int iFieldIndex);
}

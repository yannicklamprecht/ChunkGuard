package de.ysl3000.chunkguard.lib.visualisation.locationmanagement;

public class BorderValidator extends AbstractValidator
{
    @Override
    public boolean isValid(final int x, final int y, final int z) {
        return (x <= 15 && x >= 0 && (z == 0 || z == 15)) || (z <= 15 && z >= 0 && (x == 0 || x == 15));
    }
}

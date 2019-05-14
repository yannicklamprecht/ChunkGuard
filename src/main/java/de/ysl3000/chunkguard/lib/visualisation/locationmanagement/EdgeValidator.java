package de.ysl3000.chunkguard.lib.visualisation.locationmanagement;

public class EdgeValidator extends AbstractValidator
{
    @Override
    public boolean isValid(final int x, final int y, final int z) {
        return ((z == 0 || z == 15) && x % 2 == 0) || ((x == 0 || x == 15) && z % 2 == 0);
    }
}

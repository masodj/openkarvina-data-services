package cz.technecium.openkarvinadataservices.repository;


import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import cz.technecium.openkarvinadataservices.domain.Player;
import cz.technecium.openkarvinadataservices.domain.PlayerIdentifier;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.core.io.Resource;

/**
 *
 * @author msobek
 */
public class CzechRatingListRepository extends AbstractPlayerRepository implements PlayerRepository {

    private HSSFSheet sheet;

    public CzechRatingListRepository(Resource resource) {
        super(resource);
    }

    protected void readResource() throws IOException{
        InputStream file = resource.getInputStream();
        HSSFWorkbook workbook = new HSSFWorkbook(file);
        sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Player player = readPlayer(row);
            players.add(player);
        }
    }

    private Player readPlayer(Row row) {
        Player p = new Player();
        p.setClub(getCellByColumnName(ColumnName.CLUB, row));
        p.setName(getCellByColumnName(ColumnName.NAME, row));
        p.setTitle(getCellByColumnName(ColumnName.FIDE_TITLE, row));
        PlayerIdentifier playerIdentifier = new PlayerIdentifier();
        playerIdentifier.setCrId((int) (Double.parseDouble(getCellByColumnName(ColumnName.LOC_ID, row))));
        playerIdentifier.setFideId((int) (Double.parseDouble(getCellByColumnName(ColumnName.FIDE_ID, row))));
        p.setPlayerIdentifier(playerIdentifier);
        p.setFideRating((int) (Double.parseDouble(getCellByColumnName(ColumnName.FIDE_ELO, row))));
        p.setCzRating((int) (Double.parseDouble(getCellByColumnName(ColumnName.LOC_ELO, row))));
        return p;
    }

    private String getCellByColumnName(ColumnName name, Row row) {
        Row r = sheet.getRow(0);
        int patchColumn = -1;
        for (int cn = 0; cn < r.getLastCellNum(); cn++) {
            Cell c = r.getCell(cn);
            if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
                continue;
            }
            if (c.getCellType() == Cell.CELL_TYPE_STRING) {
                String text = c.getStringCellValue();
                if (name.getColumnName().equals(text)) {
                    patchColumn = cn;
                    break;
                }
            }
        }
        Cell cell = row.getCell(patchColumn);
        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            return cell.getStringCellValue();
        }

        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return cell.getNumericCellValue() + "";
        }
        return "";
    }
}
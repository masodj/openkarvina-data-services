package cz.technecium.openkarvinadataservices.repository;


import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import cz.technecium.openkarvinadataservices.domain.Player;
import cz.technecium.openkarvinadataservices.domain.PlayerIdentifier;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.core.io.Resource;

/**
 *
 * @author msobek
 */
public class CzechRatingListRepository extends AbstractPlayerRepository implements PlayerRepository {

    private static final String CLUB_NAME_NO_CZE_FED = "Cizinci";

    public CzechRatingListRepository(Resource resource) {
        super(resource);
    }

    protected void readResource() throws IOException{
        InputStream file = resource.getInputStream();
        HSSFWorkbook workbook = new HSSFWorkbook(file);
        HSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Player player = readPlayer(sheet, row);
            players.add(player);
        }
    }

    private Player readPlayer(final HSSFSheet sheet, Row row) {
        Player player = new Player();
        player.setClub(getCellByColumnName(sheet, ColumnName.CLUB, row).equals(CLUB_NAME_NO_CZE_FED) ? "" : getCellByColumnName(sheet, ColumnName.CLUB, row));
        player.setName(getCellByColumnName(sheet, ColumnName.NAME, row));
        player.setTitle(getCellByColumnName(sheet, ColumnName.FIDE_TITLE, row));
        player.setFederation(getCellByColumnName(sheet, ColumnName.FEDERATION, row));
        String birthday = getCellByColumnName(sheet, ColumnName.BIRTHDAY, row);
        player.setBirthday(birthday.startsWith("00.00.") ? birthday.substring(6) : birthday);
        PlayerIdentifier playerIdentifier = new PlayerIdentifier();

        String crId = getCellByColumnName(sheet, ColumnName.LOC_ID, row);
        playerIdentifier.setCrId(crId.isEmpty() ? null : (int) Double.parseDouble(crId));

        String fideId = getCellByColumnName(sheet, ColumnName.FIDE_ID, row);
        playerIdentifier.setFideId(fideId.isEmpty() ? null : (int) Double.parseDouble(fideId));

        player.setPlayerIdentifier(playerIdentifier);

        player.setFideRating((int) (Double.parseDouble(getCellByColumnName(sheet, ColumnName.FIDE_ELO, row))));
        player.setCzRating((int) (Double.parseDouble(getCellByColumnName(sheet, ColumnName.LOC_ELO, row))));
        return player;
    }

    private String getCellByColumnName(final HSSFSheet sheet, ColumnName name, Row row) {
        Row r = sheet.getRow(0);
        int patchColumn = -1;
        for (int cn = 0; cn < r.getLastCellNum(); cn++) {
            Cell c = r.getCell(cn);
            if (c == null || c.getCellType() == CellType.BLANK) {
                continue;
            }
            if (c.getCellType() == CellType.STRING) {
                String text = c.getStringCellValue();
                if (name.getColumnName().equals(text)) {
                    patchColumn = cn;
                    break;
                }
            }
        }
        Cell cell = row.getCell(patchColumn);
        if (cell == null) {
            return "";
        }

        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        }

        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue() + "";
        }
        return "";
    }


    @Override
    public Player findPlayerById(final long id) {
        return players.stream()
                .filter(player -> player.getPlayerIdentifier().getCrId() == id)
                .findFirst()
                .orElseThrow();
    }
}
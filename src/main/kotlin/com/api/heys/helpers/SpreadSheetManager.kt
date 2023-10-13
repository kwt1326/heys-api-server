package com.api.heys.helpers

import com.api.heys.constants.enums.ContentType
import com.api.heys.domain.channel.dto.GetChannelUserRelItemData
import com.api.heys.domain.content.dto.CreateExtraContentData
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.DateUtil
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class SpreadSheetManager<FILETYPE>(
    private val file: FILETYPE?,
) {
    private fun getCellValue(cell: Cell): String {
        return when (cell.cellType) {
            CellType.NUMERIC ->
                if (DateUtil.isCellDateFormatted(cell))
                    LocalDateTime.ofInstant(cell.dateCellValue.toInstant(), ZoneId.systemDefault()).toString()
                else
                    cell.numericCellValue.toString()

            CellType.STRING -> cell.stringCellValue.toString()
            CellType.BOOLEAN -> cell.booleanCellValue.toString()
            CellType.ERROR -> cell.errorCellValue.toString()
            else -> ""
        }
    }

    /**
     * .xlsx
     * Extra Contents Excel Data Order
     * type category contents_title host cover_image_URL team start_date end_date keyword target benefit contents_detail ask apply_links
     * */
    fun excelDataConvertToExtraContentDTOs(): List<CreateExtraContentData> {
        if (file !is MultipartFile) return listOf()

        val file = this.file as MultipartFile
        val result = mutableListOf<CreateExtraContentData>()

        XSSFWorkbook(file.inputStream).use {
            val sheet = it.getSheetAt(0) // 항상 시트는 1개일 경우를 상정하고 파싱한다.

            run {
                sheet.rowIterator().forEach { row ->
                    // table head 제외
                    if (row.rowNum == 0) return@forEach

                    // type 이 없는 row 부터는 유효하지 않은 행이므로 완전히 break 한다.
                    if (row.getCell(0).cellType != CellType.STRING) return@run

                    val cellData = mutableListOf<String>()

                    // 유효한 컬럼은 index 12 까지 (apply_links)
                    row.cellIterator().forEach { cell -> if (cell.columnIndex <= 12) cellData.add(getCellValue(cell)) }

                    val type =
                        if (cellData.getOrNull(0) != null)
                            if (cellData[0] == "contest")
                                ContentType.Contest
                            else ContentType.Extracurricular
                        else return@forEach

                    val interests =
                        if (cellData.getOrNull(1) != null)
                            cellData[1].split(", ").toMutableSet()
                        else
                            mutableSetOf()

                    val title =
                        if (cellData.getOrNull(2) != null)
                            cellData[2]
                        else return@forEach

                    val company =
                        if (cellData.getOrNull(3) != null)
                            cellData[3]
                        else return@forEach

                    val thumbnailUri =
                        if (cellData.getOrNull(4) != null)
                            cellData[4]
                        else ""

                    val startDate =
                        if (cellData.getOrNull(5) != null)
                            LocalDateTime.parse(cellData[5])
                        else return@forEach

                    val endDate =
                        if (cellData.getOrNull(6) != null)
                            LocalDateTime.parse(cellData[6])
                        else return@forEach

//                TODO: index = 7 (keyword) 처리 보류
//                val keywords =
//                    if (cellData.getOrNull(7) != null)
//                        cellData[7].split(", ").toMutableSet()
//                    else return@forEach

                    val target =
                        if (cellData.getOrNull(8) != null)
                            cellData[8]
                        else "누구나 지원 가능"

                    val benefit =
                        if (cellData.getOrNull(9) != null)
                            cellData[9]
                        else "-"

                    val contentText =
                        if (cellData.getOrNull(10) != null)
                            cellData[10]
                        else ""

                    val contact =
                        if (cellData.getOrNull(11) != null)
                            cellData[11]
                        else ""

                    val linkUrl =
                        if (cellData.getOrNull(12) != null)
                            cellData[12]
                        else ""

                    result.add(
                        CreateExtraContentData(
                            type = type, // type
                            interests = interests, // category
                            title = title, // contents_title
                            company = company, // host
                            thumbnailUri = thumbnailUri, // cover_image_URL
                            startDate = startDate, // start_date
                            endDate = endDate, // end_date
                            target = target, // target
                            benefit = benefit, // benefit
                            contentText = contentText, // contents_detail
                            contact = contact, // ask
                            linkUri = linkUrl, // apply_links
                            previewImgUri = thumbnailUri
                        )
                    )
                }
            }
        }

        return result
    }

    private fun settingHeaderForChannelJoinRefuseReasons(workbook: XSSFWorkbook, sheet: Sheet) {
        // sheet header row
        val headerRow = sheet.createRow(0)
        val font = workbook.createFont()
        font.bold = true
        font.fontName = "Arial"

        val cellStyle: CellStyle = workbook.createCellStyle()
        cellStyle.setFont(font)

        val headerString: List<String> = listOf("채널명", "휴대폰", "유저이름", "거절사유")
        var columnNum = 0

        headerString.forEach { it2 ->
            val cell: Cell = headerRow.createCell(columnNum++)
            cell.setCellValue(it2)
            cell.cellStyle = cellStyle
        }
    }

    private fun populateCellForChannelJoinRefuseReasons(
        columnNum: Int, sheet: Sheet, row: Row, value: String, cellStyle: CellStyle
    ) {
        val cell: Cell = row.createCell(columnNum);
        cell.cellStyle = cellStyle
        cell.setCellValue(value)
        sheet.autoSizeColumn(columnNum)
    }

    private fun populateDataForChannelJoinRefuseReasons(workbook: XSSFWorkbook, sheet: Sheet, data: List<GetChannelUserRelItemData>) {
        var rowNum = 1
        val font = workbook.createFont()
        font.fontName = "Arial"

        val cellStyle: CellStyle = workbook.createCellStyle()
        cellStyle.setFont(font)

        data.forEach {
            var columnNum = 0
            val row: Row = sheet.createRow(rowNum)
            populateCellForChannelJoinRefuseReasons(columnNum++, sheet, row, it.channelName, cellStyle)
            populateCellForChannelJoinRefuseReasons(columnNum++, sheet, row, it.username, cellStyle)
            populateCellForChannelJoinRefuseReasons(columnNum++, sheet, row, it.phone ?: "", cellStyle)
            populateCellForChannelJoinRefuseReasons(columnNum, sheet, row, it.refuseReason ?: "", cellStyle)
            rowNum++
        }
    }

    fun exportChannelJoinRefuseReasons(data: List<GetChannelUserRelItemData>): ByteArrayInputStream {
        XSSFWorkbook().use {
            val sheet: Sheet = it.createSheet("거절사유")

            settingHeaderForChannelJoinRefuseReasons(workbook = it, sheet)
            populateDataForChannelJoinRefuseReasons(workbook = it, sheet, data)

            try {
                val byteArrayOutputStream = ByteArrayOutputStream()
                it.write(byteArrayOutputStream)
                it.close()

                return ByteArrayInputStream(byteArrayOutputStream.toByteArray())
            } catch (e: Exception) {
                e.printStackTrace()
                throw RuntimeException(e.message)
            }
        }
    }
}
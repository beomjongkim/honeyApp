package com.dmonster.darling.honey.util.common

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

/*    DBHelper 생성자로 관리할 DB이름과 버전 정보를 받음    */
class DBHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int): SQLiteOpenHelper(context, name, factory, version) {

    /*    DB를 새로 생성할 때 호출되는 함수    */
    override fun onCreate(db: SQLiteDatabase?) {
        // 새로운 테이블 생성
        // 이름은 Honey이고 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과 honey 문자열 컬럼으로 구성된 테이블을 생성
        db?.execSQL("CREATE TABLE Honey (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id TEXT , " +
                "user_password TEXT , " +
                "user_type TEXT , " +
                "user_mb TEXT , " +
                "user_gender TEXT , " +
                "user_profile TEXT , " +
                "user_dormant TEXT , " +
                "user_recommend TEXT);")
    }

    /*    DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수    */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ".plus("Honey"))
        onCreate(db)
    }

    fun insertID(id: String?, password: String?, type: String?, mb: String?, gender: String?, profile: String?, dormant: String?, recommend: String?) {
        /*    읽고 쓰기가 가능하게 DB열기    */
        val db = writableDatabase

        /*    DB에 입력한 값으로 행 추가    */
        val values = ContentValues()
        values.put("user_id", id)
        values.put("user_password", password)
        values.put("user_type", type)
        values.put("user_mb", mb)
        values.put("user_gender", gender)
        values.put("user_profile", profile)
        values.put("user_dormant", dormant)
        values.put("user_recommend", recommend)
        db.execSQL("INSERT INTO Honey VALUES(null, '$id', '$password', '$type', '$mb', '$gender', '$profile', '$dormant', '$recommend');")
        db.close()
    }

    fun update(memo: String) {
        /*    읽고 쓰기가 가능하게 DB열기    */
        val db = writableDatabase

        /*    입력한 항목과 일치하는 행의 정보 수정    */
        db.execSQL("UPDATE Honey SET memo = '$memo';")
        db.close()
    }

    fun delete(memo: String) {
        /*    읽고 쓰기가 가능하게 DB열기    */
        val db = writableDatabase

        /*    입력한 항목과 일치하는 행 삭제    */
        db.execSQL("DELETE FROM Honey WHERE memo='$memo';")
        db.close()
    }

    fun getResult(columnName: String?): String {
        /*    읽고 쓰기가 가능하게 DB열기    */
        val db = readableDatabase
        var result = ""

        /*    DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력    */
        val cursor = db.rawQuery("SELECT * FROM Honey", null)
        /*val cursor = db.query("Honey", null, null, null, null, null, null)*/
        while(cursor.moveToNext()) {
            result += (cursor.getString(0) + ". " + cursor.getString(1) + "\n")
            /*val tempID = cursor.getString(cursor.getColumnIndex("user_id"))
            val tempPW = cursor.getString(cursor.getColumnIndex("user_password"))*/
        }

        return result
    }

}

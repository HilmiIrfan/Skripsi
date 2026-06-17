package create_structure;

import com.github.javafaker.Faker;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("unused")
public class HBaseClientStructure {

        public static void main(String[] args) throws IOException {

                Configuration conf = HBaseConfiguration.create();
                HBaseCustomClient client = new HBaseCustomClient(conf);

                // ==============================================================================================
                // CREATE COLLECTION
                // ==============================================================================================

                // Master Table

                // ==============================================================================================
                TableName tableKonsentrasiKeahlian = TableName.valueOf("konsentrasiKeahlians");
                String[] konsentrasiKeahlian = { "main", "programKeahlian", "detail" };
                client.deleteTable(tableKonsentrasiKeahlian);
                client.createTable(tableKonsentrasiKeahlian, konsentrasiKeahlian);

                TableName tableProgramKeahlian = TableName.valueOf("programKeahlians");
                String[] programKeahlian = { "main", "bidangKeahlian", "detail" };
                client.deleteTable(tableProgramKeahlian);
                client.createTable(tableProgramKeahlian, programKeahlian);

                // Create Table Bidang Keahlian
                TableName tableBidangKeahlian = TableName.valueOf("bidangKeahlians");
                String[] bidangKeahlian = { "main", "detail" };
                client.deleteTable(tableBidangKeahlian);
                client.createTable(tableBidangKeahlian, bidangKeahlian);

                // Create Table Sekolah
                TableName tableSchool = TableName.valueOf("schools");
                String[] school = { "main", "detail" };
                client.deleteTable(tableSchool);
                client.createTable(tableSchool, school);

                // Create Table Profil Sekolah
                TableName tableSchoolProfile = TableName.valueOf("school-profiles");
                String[] schoolprofile = { "main", "school", "detail" };
                client.deleteTable(tableSchoolProfile);
                client.createTable(tableSchoolProfile, schoolprofile);

                // ==============================================================================================

                // Table Family Sekolah

                // ==============================================================================================
                // Create Table Bidang Keahlian Sekolah
                TableName tableBidangKeahlianSekolah = TableName.valueOf("bidangKeahlianSekolah");
                String[] bidangKeahlianSekolah = { "main", "school", "bidangKeahlian", "detail" };
                client.deleteTable(tableBidangKeahlianSekolah);
                client.createTable(tableBidangKeahlianSekolah, bidangKeahlianSekolah);

                // Create Table Program Keahlian Sekolah
                TableName tableProgramKeahlianSekolah = TableName.valueOf("programKeahlianSekolah");
                String[] programKeahlianSekolah = { "main", "school", "programKeahlian", "detail" };
                client.deleteTable(tableProgramKeahlianSekolah);
                client.createTable(tableProgramKeahlianSekolah, programKeahlianSekolah);

                // Create Table Konsentrasi Keahlian Sekolah
                TableName tableKonsentrasiKeahlianSekolah = TableName.valueOf("konsentrasiKeahlianSekolah");
                String[] konsentrasiKeahlianSekolah = { "main", "school", "konsentrasiKeahlian", "detail" };
                client.deleteTable(tableKonsentrasiKeahlianSekolah);
                client.createTable(tableKonsentrasiKeahlianSekolah, konsentrasiKeahlianSekolah);

                // Create Tabel Kelas
                TableName tableKelas = TableName.valueOf("kelas");
                String[] kelas = { "main", "school", "detail" };
                client.deleteTable(tableKelas);
                client.createTable(tableKelas, kelas);

                // Create Tabel Mata Pelajaran
                TableName tableMapel = TableName.valueOf("mapels");
                String[] mapel = { "main", "school", "tahunAjaran", "semester", "kelas", "detail" };
                client.deleteTable(tableMapel);
                client.createTable(tableMapel, mapel);

                // Create Tabel Modul
                TableName tableModul = TableName.valueOf("modul");
                String[] modul = { "main", "school", "tahunAjaran", "semester", "kelas", "mapel", "detail" };
                client.deleteTable(tableModul);
                client.createTable(tableModul, modul);

                // Create Table Taksonomi
                TableName tableTaksonomi = TableName.valueOf("taksonomi");
                String[] taksonomi = { "main", "school", "detail" };
                client.deleteTable(tableTaksonomi);
                client.createTable(tableTaksonomi, taksonomi);

                TableName tableJadwal = TableName.valueOf("jadwalPelajarans");
                String[] jadwal = { "main", "lecture", "mapel", "detail" };
                client.deleteTable(tableJadwal);
                client.createTable(tableJadwal, jadwal);

                // Create Tabel Tahun Ajaran
                TableName tableTahun = TableName.valueOf("tahunAjaran");
                String[] tahun = { "main", "school", "detail" };
                client.deleteTable(tableTahun);
                client.createTable(tableTahun, tahun);

                // Create Tabel Semester
                TableName tableSemester = TableName.valueOf("semester");
                String[] semester = { "main", "school", "detail" };
                client.deleteTable(tableSemester);
                client.createTable(tableSemester, semester);

                // Create Tabel Elemen Pembelajaran
                TableName tableElemen = TableName.valueOf("elemen");
                String[] elemen = { "main", "school", "tahunAjaran", "mapel", "kelas", "semester",
                                "konsentrasiKeahlianSekolah",
                                "detail" };
                client.deleteTable(tableElemen);
                client.createTable(tableElemen, elemen);

                // Create Tabel Capaian Pembelajaran
                TableName tableAcp = TableName.valueOf("acp");
                String[] acp = { "main", "school", "tahunAjaran", "mapel", "kelas", "semester",
                                "konsentrasiKeahlianSekolah",
                                "elemen",
                                "detail" };
                client.deleteTable(tableAcp);
                client.createTable(tableAcp, acp);

                // Create Tabel Tujuan Pembelajaran
                TableName tableAtp = TableName.valueOf("atp");
                String[] atp = { "main", "school", "tahunAjaran", "mapel", "kelas", "semester",
                                "konsentrasiKeahlianSekolah", "elemen", "acp",
                                "detail" };
                client.deleteTable(tableAtp);
                client.createTable(tableAtp, atp);

                // Create Table Jenis Soal
                TableName tableJenisSoal = TableName.valueOf("jenisSoal");
                String[] jenisSoal = { "main", "school", "detail" };
                client.deleteTable(tableJenisSoal);
                client.createTable(tableJenisSoal, jenisSoal);

                // Create Table Soal Ujian
                TableName tableSoalUjian = TableName.valueOf("soalUjian");
                String[] soalUjian = { "main", "school", "konsentrasiKeahlianSekolah", "taksonomi", "user", "detail" };
                client.deleteTable(tableSoalUjian);
                client.createTable(tableSoalUjian, soalUjian);

                // Create Table Bank Soal
                TableName tableSoalBank = TableName.valueOf("bankSoal");
                String[] soalBank = { "main", "school", "soalUjian", "taksonomi", "tahunAjaran", "mapel", "kelas",
                                "semester", "elemen", "acp",
                                "konsentrasiKeahlianSekolah", "atp", "detail" };
                client.deleteTable(tableSoalBank);
                client.createTable(tableSoalBank, soalBank);

                // Create Table Ujian
                TableName tableUjian = TableName.valueOf("ujian");
                String[] ujianColumns = {
                                "main", "tahunAjaran", "kelas", "semester", "mapel",
                                "taksonomi", "konsentrasiKeahlianSekolah", "school", "createdBy", "bankSoalList",
                                "detail"
                };
                client.deleteTable(tableUjian);
                client.createTable(tableUjian, ujianColumns);

                // Create Table HasilUjian
                TableName tableHasilUjian = TableName.valueOf("hasil_ujian");
                String[] hasilUjianColumns = {
                                "main", "peserta", "ujian", "school", "analytics", "security", "detail"
                };
                client.deleteTable(tableHasilUjian);
                client.createTable(tableHasilUjian, hasilUjianColumns);

                // Create Table UjianSession
                TableName tableUjianSession = TableName.valueOf("ujian_session");
                String[] ujianSessionColumns = {
                                "main", "peserta", "ujian", "school", "navigation", "tracking", "security", "detail"
                };
                client.deleteTable(tableUjianSession);
                client.createTable(tableUjianSession, ujianSessionColumns);

                // Create Table CheatDetection
                TableName tableCheatDetection = TableName.valueOf("cheat_detection");
                String[] cheatDetectionColumns = {
                                "main", "peserta", "ujian", "school", "detection", "evidence", "frontend", "timing",
                                "action", "status", "detail"
                };
                client.deleteTable(tableCheatDetection);
                client.createTable(tableCheatDetection, cheatDetectionColumns);

                // Create Table UjianAnalysis
                TableName tableUjianAnalysis = TableName.valueOf("ujian_analysis");
                String[] ujianAnalysisColumns = {
                                "main", "ujian", "school", "descriptive", "analysis", "cheating", "recommendation",
                                "metadata", "detail"
                };
                client.deleteTable(tableUjianAnalysis);
                client.createTable(tableUjianAnalysis, ujianAnalysisColumns);

                // // Create Table Jawaban Siswa
                // TableName tableJawaban = TableName.valueOf("jawabanSiswa");
                // String[] jawaban = { "main", "school", "soal", "detail", "timing" };
                // client.deleteTable(tableJawaban);
                // client.createTable(tableJawaban, jawaban);

                // // Create Table Ujian
                // TableName tableUjian = TableName.valueOf("ujian");
                // String[] ujian = { "main", "school", "soalBank", "peserta", "config" };
                // client.deleteTable(tableUjian);
                // client.createTable(tableUjian, ujian);

                // Batas Kode Baru
                // Kode Lama

                // // Create Tabel Season
                // TableName tableSeason = TableName.valueOf("seasons");
                // String[] season = { "main", "bidangKeahlian", "programKeahlian",
                // "konsentrasiKeahlianSekolah", "kelas",
                // "tahunAjaran",
                // "student", "lecture", "jadwalPelajaran", "detail" };
                // client.deleteTable(tableSeason);
                // client.createTable(tableSeason, season);

                // Create Tabel Bab
                // TableName tableChapter = TableName.valueOf("chapters");
                // String[] chapters = { "main", "subject", "detail" };
                // client.deleteTable(tableChapter);
                // client.createTable(tableChapter, chapters);

                // // Create Tabel Mata Kuliah
                // TableName tableSubject = TableName.valueOf("subjects");
                // String[] subjects = { "main", "study_program", "subject_group", "detail" };
                // client.deleteTable(tableSubject);
                // client.createTable(tableSubject, subjects);

                // // Create Tabel Rumpun Mata Kuliah
                // TableName tableSubjectGroup = TableName.valueOf("subject_groups");
                // String[] subjectGroups = { "main", "detail" };
                // client.deleteTable(tableSubjectGroup);
                // client.createTable(tableSubjectGroup, subjectGroups);

                // // Create Tabel Dosen
                // TableName tableLecture = TableName.valueOf("lectures");
                // String[] lectures = { "main", "bidangKeahlian", "programKeahlian",
                // "konsentrasiKeahlian", "religion",
                // "detail" };
                // client.deleteTable(tableLecture);
                // client.createTable(tableLecture, lectures);

                // Create Tabel Mahasiswa
                TableName tableStudent = TableName.valueOf("students");
                String[] students = { "main", "bidangKeahlian", "programKeahlian",
                                "konsentrasiKeahlian", "religion",
                                "detail" };
                client.deleteTable(tableStudent);
                client.createTable(tableStudent, students);

                // // Create Tabel RPS
                // TableName tableRPS = TableName.valueOf("rps");
                // String[] RPS = { "main", "learning_media_softwares",
                // "learning_media_hardwares", "requirement_subjects",
                // "study_program", "subject", "dev_lecturers", "teaching_lecturers",
                // "coordinator_lecturers",
                // "ka_study_program",
                // "detail" };
                // client.deleteTable(tableRPS);
                // client.createTable(tableRPS, RPS);

                // // Create Tabel Detail RPS
                // TableName tableRPSDetail = TableName.valueOf("rps_details");
                // String[] RPSDetails = { "main", "rps", "learning_materials", "form_learning",
                // "learning_methods",
                // "assignments",
                // "estimated_times", "student_learning_experiences", "assessment_criterias",
                // "appraisal_forms",
                // "assessment_indicators", "detail" };
                // client.deleteTable(tableRPSDetail);
                // client.createTable(tableRPSDetail, RPSDetails);

                // // Create Table Pustaka
                // TableName tableReference = TableName.valueOf("references");
                // String[] references = { "main", "detail" };
                // client.deleteTable(tableReference);
                // client.createTable(tableReference, references);

                // Create Table Media Pembelajaran
                // TableName tableLearningMedia = TableName.valueOf("learning_medias");
                // String[] learningMedias = { "main", "detail" };
                // client.deleteTable(tableLearningMedia);
                // client.createTable(tableLearningMedia, learningMedias);

                // // Create Table Agama
                // TableName tableReligion = TableName.valueOf("religions");
                // String[] religions = { "main", "detail" };
                // client.deleteTable(tableReligion);
                // client.createTable(tableReligion, religions);

                // // Create Table Jurusan
                // TableName tableDepartment = TableName.valueOf("departments");
                // String[] departments = { "main", "detail" };
                // client.deleteTable(tableDepartment);
                // client.createTable(tableDepartment, departments);

                // // Create Table Prodi
                // TableName tableStudyProgram = TableName.valueOf("study_programs");
                // String[] studyPrograms = { "main", "department", "detail" };
                // client.deleteTable(tableStudyProgram);
                // client.createTable(tableStudyProgram, studyPrograms);

                // Create Table Users
                TableName tableUser = TableName.valueOf("users");
                String[] users = { "main", "school", "detail" };
                client.deleteTable(tableUser);
                client.createTable(tableUser, users);

                // // Create Table Bentuk Penilaian
                // TableName tableAppraisalForm = TableName.valueOf("appraisal_forms");
                // String[] appraisalForms = { "main", "detail" };
                // client.deleteTable(tableAppraisalForm);
                // client.createTable(tableAppraisalForm, appraisalForms);

                // // Create Tabel Kriteria Penilaian
                // TableName tableAssessmentCriteria =
                // TableName.valueOf("assessment_criterias");
                // String[] assessmentCriterias = { "main", "detail" };
                // client.deleteTable(tableAssessmentCriteria);
                // client.createTable(tableAssessmentCriteria, assessmentCriterias);

                // // Create Tabel Bentuk Pembelajaran
                // TableName tableFormLearning = TableName.valueOf("form_learnings");
                // String[] formLearnings = { "main", "detail" };
                // client.deleteTable(tableFormLearning);
                // client.createTable(tableFormLearning, formLearnings);

                // // Create Tabel Metode Pembelajaran
                // TableName tableLearningMethod = TableName.valueOf("learning_methods");
                // String[] learningMethods = { "main", "detail" };
                // client.deleteTable(tableLearningMethod);
                // client.createTable(tableLearningMethod, learningMethods);

                // // Create Tabel Pertanyaan
                // TableName tableQuestion = TableName.valueOf("questions");
                // String[] questions = { "main", "rps_detail", "detail" };
                // client.deleteTable(tableQuestion);
                // client.createTable(tableQuestion, questions);

                // // Create Tabel Jawaban
                // TableName tableAnswer = TableName.valueOf("answers");
                // String[] answers = { "main", "question", "detail" };
                // client.deleteTable(tableAnswer);
                // client.createTable(tableAnswer, answers);

                // // Create Tabel Ujian
                // TableName tableExam = TableName.valueOf("exams");
                // String[] exams = { "main", "rps", "questions", "detail" };
                // client.deleteTable(tableExam);
                // client.createTable(tableExam, exams);

                // // Create Tabel Kuis
                // TableName tableQuizzes = TableName.valueOf("quizzes");
                // String[] quizzes = { "main", "rps", "questions", "detail" };
                // client.deleteTable(tableQuizzes);
                // client.createTable(tableQuizzes, quizzes);
                // // Create Tabel Pengumuman Kuis
                // TableName tableQuizzesAnnouncement =
                // TableName.valueOf("quizzes_announcement");
                // String[] quizzes_announcement = { "main", "rps", "questions", "detail" };
                // client.deleteTable(tableQuizzesAnnouncement);
                // client.createTable(tableQuizzesAnnouncement, quizzes_announcement);

                // // Create Tabel Latihan
                // TableName tableExcercise = TableName.valueOf("exercises");
                // String[] exercises = { "main", "rps", "questions", "detail" };
                // client.deleteTable(tableExcercise);
                // client.createTable(tableExcercise, exercises);

                // // Create Tabel Percobaan pengumpulan Ujian
                // TableName tableExamAttempts = TableName.valueOf("exam_attempts");
                // String[] examAttempts = { "main", "exam", "user", "student",
                // "student_answers", "detail" };
                // client.deleteTable(tableExamAttempts);
                // client.createTable(tableExamAttempts, examAttempts);

                // // Create Tabel Percobaan pengumpulan Kuis
                // TableName tableQuizAttempts = TableName.valueOf("quiz_attempts");
                // String[] quizAttempts = { "main", "quiz", "user", "student",
                // "student_answers", "detail" };
                // client.deleteTable(tableQuizAttempts);
                // client.createTable(tableQuizAttempts, quizAttempts);

                // // // Create Tabel Percobaan pengumpulan Latihan
                // TableName tableExerciseAttempts = TableName.valueOf("exercise_attempts");
                // String[] exerciseAttempts = { "main", "exercise", "user", "student",
                // "student_answers", "detail" };
                // client.deleteTable(tableExerciseAttempts);
                // client.createTable(tableExerciseAttempts, exerciseAttempts);

                // // Create Tabel Metode Pembelajaran
                // TableName tableExamType = TableName.valueOf("exam_types");
                // String[] examTypes = { "main", "detail" };
                // client.deleteTable(tableExamType);
                // client.createTable(tableExamType, examTypes);

                // // Create Tabel Krireria Penilaian Soal

                // TableName tableQuestionCriteria = TableName.valueOf("question_criterias");
                // String[] questionCriterias = { "main", "detail" };
                // client.deleteTable(tableQuestionCriteria);
                // client.createTable(tableQuestionCriteria, questionCriterias);

                // // Create Tabel Nilai Linguistic

                // TableName tableLinguisticValue = TableName.valueOf("linguistic_values");
                // String[] linguisticValues = { "main", "detail" };
                // client.deleteTable(tableLinguisticValue);
                // client.createTable(tableLinguisticValue, linguisticValues);

                // // Create Tabel Team Teaching
                // TableName tableTeamTeaching = TableName.valueOf("team_teachings");
                // String[] teamTeachings = { "main", "detail", "lecture", "lecture2",
                // "lecture3" };
                // client.deleteTable(tableTeamTeaching);
                // client.createTable(tableTeamTeaching, teamTeachings);

                // // Create Tabel Penilaian Soal
                // TableName tableCriteriaValue = TableName.valueOf("criteria_values");
                // String[] criteriaValues = { "main", "detail", "team_teaching", "question",
                // "user", "value1", "value2",
                // "value3",
                // "value4", "value5", "value6", "value7", "value8", "value9" };
                // client.deleteTable(tableCriteriaValue);
                // client.createTable(tableCriteriaValue, criteriaValues);

                // seeder
                // time now
                ZoneId zoneId = ZoneId.of("Asia/Jakarta");
                ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
                Instant instant = zonedDateTime.toInstant();

                // insert school
                client.insertRecord(tableSchool, "RWK001", "main", "idSchool", "RWK001");
                client.insertRecord(tableSchool, "RWK001", "main", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableSchool, "RWK001", "main", "address", "jl. raya rowokangkung");
                client.insertRecord(tableSchool, "RWK001", "detail", "created_by", "Doyatama");

                client.insertRecord(tableSchool, "TMP001", "main", "idSchool", "TMP001");
                client.insertRecord(tableSchool, "TMP001", "main", "nameSchool", "SMK Negeri 01 TEMPEH");
                client.insertRecord(tableSchool, "TMP001", "main", "address", "jl. raya tempeh");
                client.insertRecord(tableSchool, "TMP001", "detail", "created_by", "Doyatama");

                // Insert Users
                client.insertRecord(tableUser, "USR001", "main", "id", "USR001");
                client.insertRecord(tableUser, "USR001", "main", "email", "admin@gmail.com");
                client.insertRecord(tableUser, "USR001", "main", "name", "Administrator");
                client.insertRecord(tableUser, "USR001", "main", "username", "admin");
                client.insertRecord(tableUser, "USR001", "main", "password",
                                "$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2"); // password
                client.insertRecord(tableUser, "USR001", "main", "roles", "1");
                client.insertRecord(tableUser, "USR001", "main", "created_at", "2023-05-14T04:56:23.174Z");
                client.insertRecord(tableUser, "USR001", "detail", "created_by", "Doyatama");

                client.insertRecord(tableUser, "USR002", "main", "id", "USR002");
                client.insertRecord(tableUser, "USR002", "main", "email", "operator1@gmail.com");
                client.insertRecord(tableUser, "USR002", "main", "name", "Operator1");
                client.insertRecord(tableUser, "USR002", "main", "username", "operator1");
                client.insertRecord(tableUser, "USR002", "main", "password",
                                "$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2"); // password
                client.insertRecord(tableUser, "USR002", "school", "idSchool", "RWK001");
                client.insertRecord(tableUser, "USR002", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableUser, "USR002", "main", "roles", "2");
                client.insertRecord(tableUser, "USR002", "main", "created_at", "2023-05-14T04:56:23.174Z");
                client.insertRecord(tableUser, "USR002", "detail", "created_by", "Doyatama");

                client.insertRecord(tableUser, "USR003", "main", "id", "USR003");
                client.insertRecord(tableUser, "USR003", "main", "email", "operator2@gmail.com");
                client.insertRecord(tableUser, "USR003", "main", "name", "Operator2");
                client.insertRecord(tableUser, "USR003", "main", "username", "operator2");
                client.insertRecord(tableUser, "USR003", "main", "password",
                                "$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2"); // password
                client.insertRecord(tableUser, "USR003", "school", "idSchool", "TMP001");
                client.insertRecord(tableUser, "USR003", "school", "nameSchool", "SMK Negeri 01 TEMPEH");
                client.insertRecord(tableUser, "USR003", "main", "roles", "2");
                client.insertRecord(tableUser, "USR003", "main", "created_at", "2023-05-14T04:56:23.174Z");
                client.insertRecord(tableUser, "USR003", "detail", "created_by", "Doyatama");

                client.insertRecord(tableUser, "USR004", "main", "id", "USR004");
                client.insertRecord(tableUser, "USR004", "main", "email", "guru@gmail.com");
                client.insertRecord(tableUser, "USR004", "main", "name", "Guru SMK Rowokangkung");
                client.insertRecord(tableUser, "USR004", "main", "username", "gurusmk1");
                client.insertRecord(tableUser, "USR004", "main", "password",
                                "$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2"); // password
                client.insertRecord(tableUser, "USR004", "school", "idSchool", "RWK001");
                client.insertRecord(tableUser, "USR004", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableUser, "USR004", "main", "roles", "3");
                client.insertRecord(tableUser, "USR004", "main", "created_at", Instant.now().toString());
                client.insertRecord(tableUser, "USR004", "detail", "created_by", "Doyatama");

                client.insertRecord(tableUser, "USR005", "main", "id", "USR005");
                client.insertRecord(tableUser, "USR005", "main", "email", "murid@gmail.com");
                client.insertRecord(tableUser, "USR005", "main", "name", "Murid SMK Rowokangkung");
                client.insertRecord(tableUser, "USR005", "main", "username", "muridsmk1");
                client.insertRecord(tableUser, "USR005", "main", "password",
                                "$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2"); // password
                client.insertRecord(tableUser, "USR005", "school", "idSchool", "RWK001");
                client.insertRecord(tableUser, "USR005", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableUser, "USR005", "main", "roles", "5");
                client.insertRecord(tableUser, "USR005", "main", "created_at", Instant.now().toString());
                client.insertRecord(tableUser, "USR005", "detail", "created_by", "Doyatama");

                // // Insert Religions
                // client.insertRecord(tableReligion, "RLG001", "main", "id", "RLG001");
                // client.insertRecord(tableReligion, "RLG001", "main", "name", "Islam");
                // client.insertRecord(tableReligion, "RLG001", "main", "description",
                // "deskripsi agama islam");
                // client.insertRecord(tableReligion, "RLG001", "detail", "created_by",
                // "Doyatama");

                // client.insertRecord(tableReligion, "RLG002", "main", "id", "RLG002");
                // client.insertRecord(tableReligion, "RLG002", "main", "name", "Kristen");
                // client.insertRecord(tableReligion, "RLG002", "main", "description",
                // "deskripsi agama kristen");
                // client.insertRecord(tableReligion, "RLG002", "detail", "created_by",
                // "Doyatama");

                // client.insertRecord(tableReligion, "RLG003", "main", "id", "RLG003");
                // client.insertRecord(tableReligion, "RLG003", "main", "name", "Katolik");
                // client.insertRecord(tableReligion, "RLG003", "main", "description",
                // "deskripsi agama katolik");
                // client.insertRecord(tableReligion, "RLG003", "detail", "created_by",
                // "Doyatama");

                // client.insertRecord(tableReligion, "RLG004", "main", "id", "RLG004");
                // client.insertRecord(tableReligion, "RLG004", "main", "name", "Hindu");
                // client.insertRecord(tableReligion, "RLG004", "main", "description",
                // "deskripsi agama hindu");
                // client.insertRecord(tableReligion, "RLG004", "detail", "created_by",
                // "Doyatama");

                // client.insertRecord(tableReligion, "RLG005", "main", "id", "RLG005");
                // client.insertRecord(tableReligion, "RLG005", "main", "name", "Buddha");
                // client.insertRecord(tableReligion, "RLG005", "main", "description",
                // "deskripsi agama budha");
                // client.insertRecord(tableReligion, "RLG005", "detail", "created_by",
                // "Doyatama");

                // client.insertRecord(tableReligion, "RLG006", "main", "id", "RLG006");
                // client.insertRecord(tableReligion, "RLG006", "main", "name", "Kong Hu Chu");
                // client.insertRecord(tableReligion, "RLG006", "main", "description",
                // "deskripsi agama kong hu chu");
                // client.insertRecord(tableReligion, "RLG006", "detail", "created_by",
                // "Doyatama");

                // // Insert Bentuk Pembelajaran
                // client.insertRecord(tableFormLearning, "BP001", "main", "id", "BP001");
                // client.insertRecord(tableFormLearning, "BP001", "main", "name", "Daring");
                // client.insertRecord(tableFormLearning, "BP001", "main", "description",
                // "Pembelajaran dilakukan secara dalam jaringan / online");
                // client.insertRecord(tableFormLearning, "BP001", "detail", "created_by",
                // "Doyatama");

                // client.insertRecord(tableFormLearning, "BP002", "main", "id", "BP002");
                // client.insertRecord(tableFormLearning, "BP002", "main", "name", "Luring");
                // client.insertRecord(tableFormLearning, "BP002", "main", "description",
                // "Pembelajaran dilakukan secara diluar jaringan / offline");
                // client.insertRecord(tableFormLearning, "BP002", "detail", "created_by",
                // "Doyatama");

                // // Insert Metode Pembelajaran
                // client.insertRecord(tableLearningMethod, "MP001", "main", "id", "MP001");
                // client.insertRecord(tableLearningMethod, "MP001", "main", "name",
                // "Contextual Teaching and Learning (CTL)");
                // client.insertRecord(tableLearningMethod, "MP001", "main", "description",
                // "Pengertian dari CTL");
                // client.insertRecord(tableLearningMethod, "MP001", "detail", "created_by",
                // "Doyatama");

                // client.insertRecord(tableLearningMethod, "MP002", "main", "id", "MP002");
                // client.insertRecord(tableLearningMethod, "MP002", "main", "name", "Problem
                // Based Learning");
                // client.insertRecord(tableLearningMethod, "MP002", "main", "description",
                // "Pengertian dari PBL");
                // client.insertRecord(tableLearningMethod, "MP002", "detail", "created_by",
                // "Doyatama");

        }
}
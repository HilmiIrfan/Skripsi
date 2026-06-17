/* eslint-disable no-unused-vars */
import React from "react";
import Loading from "@/components/Loading";
import Religion from "../views/religion";
import SubjectGroup from "../views/subject-group";
import Student from "../views/student";
import Subject from "../views/subject";
import questionIndex from "../views/question-index";

const Dashboard = React.lazy(() => import("@/views/dashboard"));
const Doc = React.lazy(() => import(/*webpackChunkName:'Doc'*/ "@/views/doc"));
const Guide = React.lazy(() =>
  import(/*webpackChunkName:'Guide'*/ "@/views/guide")
);
const Explanation = React.lazy(() =>
  import(/*webpackChunkName:'Explanation'*/ "@/views/permission")
);
const AdminPage = React.lazy(() =>
  import(/*webpackChunkName:'AdminPage'*/ "@/views/permission/adminPage")
);
const GuestPage = React.lazy(() =>
  import(/*webpackChunkName:'GuestPage'*/ "@/views/permission/guestPage")
);
const EditorPage = React.lazy(() =>
  import(/*webpackChunkName:'EditorPage'*/ "@/views/permission/editorPage")
);
const RichTextEditor = React.lazy(() =>
  import(
    /*webpackChunkName:'RichTextEditor'*/ "@/views/components-demo/richTextEditor"
  )
);
const Markdown = React.lazy(() =>
  import(/*webpackChunkName:'Markdown'*/ "@/views/components-demo/Markdown")
);
const Draggable = React.lazy(() =>
  import(/*webpackChunkName:'Draggable'*/ "@/views/components-demo/draggable")
);
const KeyboardChart = React.lazy(() =>
  import(/*webpackChunkName:'KeyboardChart'*/ "@/views/charts/keyboard")
);
const LineChart = React.lazy(() =>
  import(/*webpackChunkName:'LineChart'*/ "@/views/charts/line")
);
const MixChart = React.lazy(() =>
  import(/*webpackChunkName:'MixChart'*/ "@/views/charts/mixChart")
);
const Menu1_1 = React.lazy(() =>
  import(/*webpackChunkName:'Menu1_1'*/ "@/views/nested/menu1/menu1-1")
);
const Menu1_2_1 = React.lazy(() =>
  import(
    /*webpackChunkName:'Menu1_2_1'*/ "@/views/nested/menu1/menu1-2/menu1-2-1"
  )
);
const Table = React.lazy(() =>
  import(/*webpackChunkName:'Table'*/ "@/views/table")
);
const ExportExcel = React.lazy(() =>
  import(/*webpackChunkName:'ExportExcel'*/ "@/views/excel/exportExcel")
);
const UploadExcel = React.lazy(() =>
  import(/*webpackChunkName:'UploadExcel'*/ "@/views/excel/uploadExcel")
);
const Zip = React.lazy(() => import(/*webpackChunkName:'Zip'*/ "@/views/zip"));
const Clipboard = React.lazy(() =>
  import(/*webpackChunkName:'Clipboard'*/ "@/views/clipboard")
);
const Error404 = React.lazy(() =>
  import(/*webpackChunkName:'Error404'*/ "@/views/error/404")
);
const User = React.lazy(() =>
  import(/*webpackChunkName:'User'*/ "@/views/user")
);
const SchoolProfile = React.lazy(() =>
  import(/*webpackChunkName:'SchoolProfile'*/ "@/views/school-profile")
);
const Question = React.lazy(() =>
  import(/*webpackChunkName:'Question'*/ "@/views/question")
);
const Answer = React.lazy(() =>
  import(/*webpackChunkName:'Answer'*/ "@/views/answer")
);
const Department = React.lazy(() =>
  import(/*webpackChunkName:'Department'*/ "@/views/department")
);
const StudyProgram = React.lazy(() =>
  import(/*webpackChunkName:'StudyProgram'*/ "@/views/study-program")
);
const Lecture = React.lazy(() =>
  import(/*webpackChunkName:'Lecture'*/ "@/views/lecture")
);
const RPS = React.lazy(() => import(/*webpackChunkName:'RPS'*/ "@/views/rps"));
const RPSDetail = React.lazy(() =>
  import(/*webpackChunkName:'RPS'*/ "@/views/rps-detail")
);
const FormLearning = React.lazy(() =>
  import(/*webpackChunkName:'FormLearning'*/ "@/views/form-learning")
);
const LearningMedia = React.lazy(() =>
  import(/*webpackChunkName:'LearningMedia'*/ "@/views/learning-media")
);
const LearningMethod = React.lazy(() =>
  import(/*webpackChunkName:'LearningMethod'*/ "@/views/learning-method")
);
const AssessmentCriteria = React.lazy(() =>
  import(
    /*webpackChunkName:'AssessmentCriteria'*/ "@/views/assessment-criteria"
  )
);
const QuestionCriteria = React.lazy(() =>
  import(/*webpackChunkName:'QuestionCriteria'*/ "@/views/question-criteria")
);
const LinguiticValue = React.lazy(() =>
  import(/*webpackChunkName:'LinguiticValue'*/ "@/views/linguistic-value")
);
const TeamTeaching = React.lazy(() =>
  import(/*webpackChunkName:'TeamTeaching'*/ "@/views/team-teaching")
);
const CriteriaValue = React.lazy(() =>
  import(/*webpackChunkName:'criteriaValue'*/ "@/views/criteria-value")
);
// const ListTodo = React.lazy(() =>
//   import(/*webpackChunkName:'criteriaValue'*/ "@/views/list-todo")
// );
// const ListTodoAdmin = React.lazy(() =>
//   import(/*webpackChunkName:'criteriaValue'*/ "@/views/list-todo-admin")
// );
const QuestionIndexQuiz1 = React.lazy(() =>
  import(/*webpackChunkName:'questionIndex'*/ "@/views/question-index-quiz1")
);
const QuestionIndexQuiz2 = React.lazy(() =>
  import(/*webpackChunkName:'questionIndex'*/ "@/views/question-index-quiz2")
);
const QuizGenerateQuiz1 = React.lazy(() =>
  import(/*webpackChunkName:'questionIndex'*/ "@/views/quiz-generate-quiz1")
);
const QuizGenerateQuizStep2 = React.lazy(() =>
  import(/*webpackChunkName:'questionIndex'*/ "@/views/quiz-generate-step2")
);
const QuizGenerateQuizStep3 = React.lazy(() =>
  import(/*webpackChunkName:'questionIndex'*/ "@/views/quiz-generate-step3")
);
const QuizGenerateQuizStep4 = React.lazy(() =>
  import(/*webpackChunkName:'questionIndex'*/ "@/views/quiz-generate-step4")
);
const QuizGenerateQuizStep5 = React.lazy(() =>
  import(/*webpackChunkName:'questionIndex'*/ "@/views/quiz-generate-step5")
);
const QuizGenerateQuizStep6 = React.lazy(() =>
  import(/*webpackChunkName:'questionIndex'*/ "@/views/quiz-generate-step6")
);
const CriteriaIndex = React.lazy(() =>
  import(/*webpackChunkName:'questionIndex'*/ "@/views/criteria-index")
);
const ExerciseIndex = React.lazy(() =>
  import(/*webpackChunkName:'questionIndex'*/ "@/views/exercise-index")
);
const AppraisalForm = React.lazy(() =>
  import(/*webpackChunkName:'AppraisalForm'*/ "@/views/appraisal-form")
);
const Exam = React.lazy(() =>
  import(/*webpackChunkName:'Exam'*/ "@/views/exam")
);
const Quiz = React.lazy(() =>
  import(/*webpackChunkName:'Quiz'*/ "@/views/quiz")
);
const Exercise = React.lazy(() =>
  import(/*webpackChunkName:'Exercise'*/ "@/views/exercise")
);
const ResultExam = React.lazy(() =>
  import(/*webpackChunkName:'Exam'*/ "@/views/result-exam")
);
const ResultQuiz = React.lazy(() =>
  import(/*webpackChunkName:'Quiz'*/ "@/views/result-quiz")
);
const ResultExercise = React.lazy(() =>
  import(/*webpackChunkName:'Exercise'*/ "@/views/result-exercise")
);
const StudentExam = React.lazy(() =>
  import(/*webpackChunkName:'Exam'*/ "@/views/student-exam")
);
const DoStudentExam = React.lazy(() =>
  import(/*webpackChunkName:'Exam'*/ "@/views/do-student-exam")
);
const DoStudentExercise = React.lazy(() =>
  import(/*webpackChunkName:'Exam'*/ "@/views/do-student-exercise")
);
const DoStudentQuiz = React.lazy(() =>
  import(/*webpackChunkName:'Exam'*/ "@/views/do-student-quiz")
);
const StudentQuiz = React.lazy(() =>
  import(/*webpackChunkName:'Quiz'*/ "@/views/student-quiz")
);
const StudentExercise = React.lazy(() =>
  import(/*webpackChunkName:'Exercise'*/ "@/views/student-exercise")
);
const StudentExerciseReview = React.lazy(() =>
  import(/*webpackChunkName:'Exercise'*/ "@/views/student-exercise-review")
);
// const PesertaAnalysis = React.lazy(() =>
//   import(/*webpackChunkName:'PesertaAnalysis'*/ "@/views/ujian-analysis")
// );
// const IntegrasiPesertaDashboard = React.lazy(() =>
//   import(
//     /*webpackChunkName:'IntegrasiPeserta'*/ "@/views/integrasi-ujian/IntegrasiPesertaDashboard"
//   )
// );
// const LaporanNilai = React.lazy(() =>
//   import(/*webpackChunkName:'LaporanNilai'*/ "@/views/laporan-nilai")
// );
const BidangKeahlian = React.lazy(() =>
  import(/*webpackChunkName:'BidangKeahlian'*/ "@/views/bidang-keahlian")
);
const ProgramKeahlian = React.lazy(() =>
  import(/*webpackChunkName:'ProgramKeahlian'*/ "@/views/program-keahlian")
);
const KonsentrasiKeahlian = React.lazy(() =>
  import(
    /*webpackChunkName:'KonsentrasiKeahlian'*/ "@/views/konsentrasi-keahlian"
  )
);
const BidangKeahlianSekolah = React.lazy(() =>
  import(
    /*webpackChunkName:'BidangKeahlianSekolah'*/ "@/views/bidang-keahlian-sekolah"
  )
);
const ProgramKeahlianSekolah = React.lazy(() =>
  import(
    /*webpackChunkName:'ProgramKeahlianSekolah'*/ "@/views/program-keahlian-sekolah"
  )
);
const KonsentrasiKeahlianSekolah = React.lazy(() =>
  import(
    /*webpackChunkName:'KonsentrasiKeahlianSekolah'*/ "@/views/konsentrasi-keahlian-sekolah"
  )
);
// const JadwalPelajaran = React.lazy(() =>
//   import(/*webpackChunkName:'jadwalPelajaran'*/ "@/views/jadwal-pelajaran")
// );
const Kelas = React.lazy(() =>
  import(/*webpackChunkName:'kelas'*/ "@/views/kelas")
);
const Semester = React.lazy(() =>
  import(/*webpackChunkName:'Semester'*/ "@/views/semester")
);
const TahunAjaran = React.lazy(() =>
  import(/*webpackChunkName:'TahunAjaran'*/ "@/views/tahun-ajaran")
);
const Modul = React.lazy(() =>
  import(/*webpackChunkName:'Modul'*/ "@/views/modul")
);
// const Season = React.lazy(() =>
//   import(/*webpackChunkName:'Season'*/ "@/views/season")
// );
// const Kurikulum = React.lazy(() =>
//   import(/*webpackChunkName:'Kurikulum'*/ "@/views/kurikulum")
// );
const Elemen = React.lazy(() =>
  import(/*webpackChunkName:'Result'*/ "@/views/elemen")
);
const ACP = React.lazy(() =>
  import(/*webpackChunkName:'AnalisaCapaianPembelajaran'*/ "@/views/acp")
);
const ATP = React.lazy(() =>
  import(/*webpackChunkName:'AlurTujuanPembelajaran'*/ "@/views/atp")
);

// Data Soal
const Taksonomi = React.lazy(() =>
  import(/*webpackChunkName:'Taksonomi'*/ "@/views/taksonomi")
);
const SoalUjian = React.lazy(() =>
  import(/*webpackChunkName:'SoalUjian'*/ "@/views/soal-ujian")
);
const BankSoal = React.lazy(() =>
  import(/*webpackChunkName:'BankSoal'*/ "@/views/bank-soal")
);
const Ujian = React.lazy(() =>
  import(/*webpackChunkName:'Ujian'*/ "@/views/ujian")
);
const UjianView = React.lazy(() =>
  import(/*webpackChunkName:'UjianView'*/ "@/views/ujian-view")
);
const UjianHistory = React.lazy(() =>
  import(/*webpackChunkName:'UjianHistory'*/ "@/views/ujian-history")
);
const UjianAnalysis = React.lazy(() =>
  import(/*webpackChunkName:'UjianAnalysis'*/ "@/views/ujian-analysis")
);
const UjianAnalysisDetail = React.lazy(() =>
  import(
    /*webpackChunkName:'UjianAnalysisDetail'*/ "@/views/ujian-analysis/DetailAnalysis"
  )
);
const IntegrasiUjianDashboard = React.lazy(() =>
  import(
    /*webpackChunkName:'IntegrasiUjianDashboard'*/ "@/views/integrasi-ujian/IntegrasiUjianDashboard"
  )
);
const ReportNilai = React.lazy(() =>
  import(/*webpackChunkName:'ReportNilai'*/ "@/views/report-nilai")
);

// const Grade = React.lazy(() =>
//   import(/*webpackChunkName:'Grade'*/ "@/views/grade")
// );
const About = React.lazy(() =>
  import(/*webpackChunkName:'About'*/ "@/views/about")
);
const Bug = React.lazy(() => import(/*webpackChunkName:'Bug'*/ "@/views/bug"));

export default [
  {
    path: "/dashboard",
    component: Dashboard,
    roles: [
      "ROLE_ADMINISTRATOR",
      "ROLE_OPERATOR",
      "ROLE_TEACHER",
      "ROLE_DUDI",
      "ROLE_STUDENT",
    ],
  },
  {
    path: "/doc",
    component: Doc,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER", "ROLE_STUDENT"],
  },
  {
    path: "/guide",
    component: Guide,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/permission/explanation",
    component: Explanation,
    roles: ["ROLE_OPERATOR"],
  },
  {
    path: "/permission/adminPage",
    component: AdminPage,
    roles: ["ROLE_OPERATOR"],
  },
  {
    path: "/permission/guestPage",
    component: GuestPage,
    roles: ["ROLE_STUDENT"],
  },
  {
    path: "/permission/editorPage",
    component: EditorPage,
    roles: ["ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/components/richTextEditor",
    component: RichTextEditor,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/components/Markdown",
    component: Markdown,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/components/draggable",
    component: Draggable,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/charts/keyboard",
    component: KeyboardChart,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/charts/line",
    component: LineChart,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/charts/mix-chart",
    component: MixChart,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/nested/menu1/menu1-1",
    component: Menu1_1,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/nested/menu1/menu1-2/menu1-2-1",
    component: Menu1_2_1,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/table",
    component: Table,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/excel/export",
    component: ExportExcel,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/excel/upload",
    component: UploadExcel,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/zip",
    component: Zip,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/clipboard",
    component: Clipboard,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/user",
    component: User,
    roles: ["ROLE_ADMINISTRATOR", "ROLE_OPERATOR"],
  },
  {
    path: "/school-profile",
    component: SchoolProfile,
    roles: ["ROLE_ADMINISTRATOR"],
  },
  {
    path: "/bidang-keahlian",
    component: BidangKeahlian,
    roles: ["ROLE_ADMINISTRATOR"],
  },
  {
    path: "/program-keahlian",
    component: ProgramKeahlian,
    roles: ["ROLE_ADMINISTRATOR"],
  },
  {
    path: "/konsentrasi-keahlian",
    component: KonsentrasiKeahlian,
    roles: ["ROLE_ADMINISTRATOR", "ROLE_OPERATOR"],
  },
  {
    path: "/bidang-keahlian-sekolah",
    component: BidangKeahlianSekolah,
    roles: ["ROLE_OPERATOR"],
  },
  {
    path: "/program-keahlian-sekolah",
    component: ProgramKeahlianSekolah,
    roles: ["ROLE_OPERATOR"],
  },
  {
    path: "/konsentrasi-keahlian-sekolah",
    component: KonsentrasiKeahlianSekolah,
    roles: ["ROLE_OPERATOR"],
  },
  // { path: "/department", component: Department, roles: ["ROLE_OPERATOR"] },
  { path: "/kelas", component: Kelas, roles: ["ROLE_OPERATOR"] },
  { path: "/semester", component: Semester, roles: ["ROLE_OPERATOR"] },
  { path: "/tahun-ajaran", component: TahunAjaran, roles: ["ROLE_OPERATOR"] },
  // { path: "/season", component: Season, roles: ["ROLE_OPERATOR"] },
  { path: "/modul", component: Modul, roles: ["ROLE_OPERATOR"] },
  { path: "/elemen", component: Elemen, roles: ["ROLE_OPERATOR"] },
  { path: "/acp", component: ACP, roles: ["ROLE_OPERATOR"] },
  { path: "/atp", component: ATP, roles: ["ROLE_OPERATOR"] },
  {
    path: "/taksonomi",
    component: Taksonomi,
    roles: ["ROLE_OPERATOR", "ROLE_TEACHER"],
  },
  {
    path: "/soal-ujian",
    component: SoalUjian,
    roles: ["ROLE_OPERATOR", "ROLE_TEACHER"],
  },
  {
    path: "/bank-soal",
    component: BankSoal,
    roles: ["ROLE_OPERATOR", "ROLE_TEACHER"],
  },
  {
    path: "/ujian",
    component: Ujian,
    roles: ["ROLE_OPERATOR", "ROLE_TEACHER"],
  },
  // ujian-view dihapus dari sini karena sudah dihandle di router utama tanpa layout
  {
    path: "/ujian-history",
    component: UjianHistory,
    roles: ["ROLE_OPERATOR", "ROLE_TEACHER", "ROLE_STUDENT"],
  },
  {
    path: "/ujian-analysis",
    component: UjianAnalysis,
    roles: ["ROLE_OPERATOR", "ROLE_TEACHER"],
  },
  {
    path: "/ujian-analysis/detail/:idUjian",
    component: UjianAnalysisDetail,
    roles: ["ROLE_OPERATOR", "ROLE_TEACHER"],
  },
  // {
  //   path: "/peserta-analysis/detail/:idPeserta/:idUjian",
  //   component: PesertaAnalysis,
  //   roles: ["ROLE_OPERATOR", "ROLE_TEACHER"],
  // },
  // {
  //   path: "/integrasi-ujian",
  //   component: IntegrasiPesertaDashboard,
  //   roles: ["ROLE_OPERATOR", "ROLE_TEACHER"],
  // },
  // {
  //   path: "/laporan-nilai",
  //   component: LaporanNilai,
  //   roles: ["ROLE_OPERATOR", "ROLE_ADMINISTRATOR"],
  // },
  {
    path: "/report-nilai",
    component: ReportNilai,
    roles: ["ROLE_OPERATOR", "ROLE_TEACHER"],
  },

  // {
  //   path: "/study-program",
  //   component: StudyProgram,
  //   roles: ["ROLE_OPERATOR"],
  // },
  // { path: "/religion", component: Religion, roles: ["ROLE_OPERATOR"] },
  // {
  //   path: "/subject-group",
  //   component: SubjectGroup,
  //   roles: ["ROLE_OPERATOR"],
  // },
  { path: "/subject", component: Subject, roles: ["ROLE_OPERATOR"] },
  // {
  //   path: "/jadwal-pelajaran",
  //   component: JadwalPelajaran,
  //   roles: ["ROLE_OPERATOR"],
  // },
  // { path: "/kurikulum", component: Kurikulum, roles: ["ROLE_OPERATOR"] },
  { path: "/lecture", component: Lecture, roles: ["ROLE_OPERATOR"] },
  // {
  //   path: "/question-criteria",
  //   component: QuestionCriteria,
  //   roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  // },
  // {
  //   path: "/team-teaching",
  //   component: TeamTeaching,
  //   roles: ["ROLE_OPERATOR"],
  // },
  // {
  //   path: "/linguistic-value",
  //   component: LinguiticValue,
  //   roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  // },
  // {
  //   path: "/criteria-value",
  //   component: CriteriaValue,
  //   roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  // },
  // {
  //   path: "/list-todo",
  //   component: ListTodo,
  //   roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  // },
  // {
  //   path: "/list-todo-admin",
  //   component: ListTodoAdmin,
  //   roles: ["ROLE_OPERATOR"],
  // },
  // {
  //   path: "/index/question/:rpsID",
  //   component : QuestionIndex,
  //   roles: ["ROLE_OPERATOR"]
  // },
  // {
  //   path: "/index/question/quiz1/:rpsID",
  //   component: QuestionIndexQuiz1,
  //   roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  // },
  // {
  //   path: "/index/question/quiz2/:rpsID",
  //   component: QuestionIndexQuiz2,
  //   roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  // },
  // {
  //   path: "/index/criteria/:questionID",
  //   component: CriteriaIndex,
  //   roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  // },
  {
    path: "/index/exercise/:exerciseID",
    component: ExerciseIndex,
    roles: ["ROLE_OPERATOR"],
  },
  { path: "/student", component: Student, roles: ["ROLE_OPERATOR"] },
  // {
  //   path: "/rps",
  //   component: RPS,
  //   roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  //   exact: true,
  // },
  // {
  //   path: "/rps/:rpsID",
  //   component: RPSDetail,
  //   roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  //   exact: true,
  // },
  {
    path: "/question",
    component: Question,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
    exact: true,
  },
  {
    path: "/rps/:rpsID/:rpsDetailID",
    component: Question,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
    exact: true,
  },
  {
    path: "/rps/:rpsID/:rpsDetailID/:questionID",
    component: Answer,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/form-learning",
    component: FormLearning,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/learning-media",
    component: LearningMedia,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/learning-method",
    component: LearningMethod,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/assessment-criteria",
    component: AssessmentCriteria,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/appraisal-form",
    component: AppraisalForm,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/setting-exam",
    component: Exam,
    roles: ["ROLE_OPERATOR"],
    exact: true,
  },
  {
    path: "/setting-quiz",
    component: Quiz,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
    exact: true,
  },
  {
    path: "/setting-exercise",
    component: Exercise,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
    exact: true,
  },
  {
    path: "/exam",
    component: StudentExam,
    roles: ["ROLE_STUDENT"],
    exact: true,
  },
  {
    path: "/quiz",
    component: StudentQuiz,
    roles: ["ROLE_STUDENT"],
    exact: true,
  },
  {
    path: "/exercise",
    component: StudentExercise,
    roles: ["ROLE_STUDENT"],
    exact: true,
  },
  {
    path: "/exercise-review/:id",
    component: StudentExerciseReview,
    roles: ["ROLE_STUDENT"],
    exact: true,
  },
  { path: "/exam/do/:id", component: DoStudentExam, roles: ["ROLE_STUDENT"] },
  { path: "/quiz/do/:id", component: DoStudentQuiz, roles: ["ROLE_STUDENT"] },
  {
    path: "/exercise/do/:id",
    component: DoStudentExercise,
    roles: ["ROLE_STUDENT"],
  },

  {
    path: "/setting-exam/result/:id",
    component: ResultExam,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/setting-quiz/result/:id",
    component: ResultQuiz,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/setting-quiz/generate-quiz/:id",
    component: QuizGenerateQuiz1,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/setting-quiz/generate-quiz-step2/:id",
    component: QuizGenerateQuizStep2,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/setting-quiz/generate-quiz-step3/:id",
    component: QuizGenerateQuizStep3,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/setting-quiz/generate-quiz-step4/:id",
    component: QuizGenerateQuizStep4,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/setting-quiz/generate-quiz-step5/:id",
    component: QuizGenerateQuizStep5,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/setting-quiz/generate-quiz-step6/:id",
    component: QuizGenerateQuizStep6,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },
  {
    path: "/setting-exercise/result/:id",
    component: ResultExercise,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  },

  // { path: "/grade", component: Grade, roles: ["ROLE_OPERATOR"] },
  {
    path: "/about",
    component: About,
    roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER", "ROLE_STUDENT"],
  },
  { path: "/bug", component: Bug, roles: ["ROLE_OPERATOR"] },
  { path: "/error/404", component: Error404 },
];

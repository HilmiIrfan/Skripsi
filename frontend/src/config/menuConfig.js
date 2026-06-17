/* eslint-disable no-unused-vars */
import {
  HomeOutlined,
  DatabaseOutlined,
  ApartmentOutlined,
  AppstoreOutlined,
  GlobalOutlined,
  BranchesOutlined,
  AuditOutlined,
  ContainerOutlined,
  ControlOutlined,
  FileDoneOutlined,
  FileSyncOutlined,
  TeamOutlined,
  SolutionOutlined,
  FileTextOutlined,
  FileSearchOutlined,
  UsergroupAddOutlined,
  RadarChartOutlined,
  FileProtectOutlined,
  CopyrightOutlined,
  BugOutlined,
} from "@ant-design/icons";
import { icon } from "leaflet";

const menuList = [
  {
    title: "Beranda",
    path: "/dashboard",
    icon: HomeOutlined,
    roles: [
      "ROLE_ADMINISTRATOR",
      "ROLE_OPERATOR",
      "ROLE_TEACHER",
      "ROLE_DUDI",
      "ROLE_STUDENT",
    ],
  },
  {
    title: "Bidang Keahlian",
    path: "/bidang-keahlian",
    icon: ApartmentOutlined,
    roles: ["ROLE_ADMINISTRATOR"],
  },
  {
    title: "Program Keahlian",
    path: "/program-keahlian",
    icon: ApartmentOutlined,
    roles: ["ROLE_ADMINISTRATOR"],
  },
  {
    title: "Konsentrasi Keahlian",
    path: "/konsentrasi-keahlian",
    icon: ApartmentOutlined,
    roles: ["ROLE_ADMINISTRATOR"],
  },
  {
    title: "Bidang Keahlian Sekolah",
    path: "/bidang-keahlian-sekolah",
    icon: ApartmentOutlined,
    roles: ["ROLE_OPERATOR"],
  },
  {
    title: "Program Keahlian Sekolah",
    path: "/program-keahlian-sekolah",
    icon: ApartmentOutlined,
    roles: ["ROLE_OPERATOR"],
  },
  {
    title: "Konsentrasi Keahlian Sekolah",
    path: "/konsentrasi-keahlian-sekolah",
    icon: ApartmentOutlined,
    roles: ["ROLE_OPERATOR"],
  },
  {
    title: "Master Data",
    icon: DatabaseOutlined,
    roles: ["ROLE_OPERATOR"],
    children: [
      // {
      //   title: "Jurusan",
      //   path: "/department",
      //   icon: ApartmentOutlined,
      //   roles: ["ROLE_OPERATOR"],
      // },
      // {
      //   title: "Prodi",
      //   path: "/study-program",
      //   icon: ApartmentOutlined,
      //   roles: ["ROLE_OPERATOR"],
      // },
      // {
      //   title: "Agama",
      //   path: "/religion",
      //   icon: GlobalOutlined,
      //   roles: ["ROLE_OPERATOR"],
      // },
      {
        title: "Tahun Ajaran",
        path: "/tahun-ajaran",
        icon: AuditOutlined,
        roles: ["ROLE_OPERATOR"],
      },
      {
        title: "Semester",
        path: "/semester",
        icon: AuditOutlined,
        roles: ["ROLE_OPERATOR"],
      },
      // {
      //   title: "Kurikulum",
      //   path: "/kurikulum",
      //   icon: BranchesOutlined,
      //   roles: ["ROLE_OPERATOR"],
      // },
      {
        title: "Kelas",
        path: "/kelas",
        icon: AuditOutlined,
        roles: ["ROLE_OPERATOR"],
      },
      {
        title: "Mata Pelajaran",
        path: "/subject",
        icon: AuditOutlined,
        roles: ["ROLE_OPERATOR"],
      },
      {
        title: "Modul",
        path: "/modul",
        icon: AuditOutlined,
        roles: ["ROLE_OPERATOR"],
      },
      // {
      //   title: "Jadwal Pelajaran",
      //   path: "/jadwal-pelajaran",
      //   icon: BranchesOutlined,
      //   roles: ["ROLE_OPERATOR"],
      // },
      // {
      //   title: "Kelas Ajaran",
      //   path: "/season",
      //   icon: AuditOutlined,
      //   roles: ["ROLE_OPERATOR"],
      // },
      {
        title: "Elemen Pembelajaran",
        path: "/elemen",
        icon: AuditOutlined,
        roles: ["ROLE_OPERATOR"],
      },
      {
        title: "Analisis Capaian Pembelajaran",
        path: "/acp",
        icon: AuditOutlined,
        roles: ["ROLE_OPERATOR"],
      },
      {
        title: "Alur Tujuan Pembelajaran",
        path: "/atp",
        icon: AuditOutlined,
        roles: ["ROLE_OPERATOR"],
      },
      // {
      //   title: "Media Pembelajaran",
      //   path: "/learning-media",
      //   icon: ApartmentOutlined,
      //   roles: ["ROLE_OPERATOR"],
      // },
      // {
      //   title: "Bentuk Pembelajaran",
      //   path: "/form-learning",
      //   icon: ContainerOutlined,
      //   roles: ["ROLE_OPERATOR"],
      // },
      // {
      //   title: "Metode Pembelajaran",
      //   path: "/learning-method",
      //   icon: ControlOutlined,
      //   roles: ["ROLE_OPERATOR"],
      // },
      // {
      //   title: "Kriteria Penilaian",
      //   path: "/assessment-criteria",
      //   icon: FileDoneOutlined,
      //   roles: ["ROLE_OPERATOR"],
      // },
      // {
      //   title: "Formulir Penilaian",
      //   path: "/appraisal-form",
      //   icon: FileSyncOutlined,
      //   roles: ["ROLE_OPERATOR"],
      // },
    ],
  },

  {
    title: "Data Soal",
    icon: DatabaseOutlined,
    roles: ["ROLE_OPERATOR", "ROLE_TEACHER"],
    children: [
      {
        title: "Taksonomi",
        path: "/taksonomi",
        icon: AuditOutlined,
        roles: ["ROLE_OPERATOR", "ROLE_TEACHER"],
      },
      {
        title: "Soal Ujian",
        path: "/soal-ujian",
        icon: AuditOutlined,
        roles: ["ROLE_OPERATOR", "ROLE_TEACHER"],
      },
      {
        title: "Bank Soal",
        path: "/bank-soal",
        icon: AuditOutlined,
        roles: ["ROLE_OPERATOR", "ROLE_TEACHER"],
      },
    ],
  },

  {
    title: " Setting Ujian",
    path: "/ujian",
    icon: SolutionOutlined,
    roles: ["ROLE_OPERATOR", "ROLE_TEACHER"],
  },

  {
    title: "Ujian Siswa",
    path: "/ujian-view",
    icon: SolutionOutlined,
    roles: ["ROLE_STUDENT"],
  },

  {
    title: "Riwayat Ujian",
    path: "/ujian-history",
    icon: FileTextOutlined,
    roles: ["ROLE_STUDENT"],
  },

  {
    title: "Analisis Ujian",
    path: "/ujian-analysis",
    icon: RadarChartOutlined,
    roles: ["ROLE_OPERATOR", "ROLE_TEACHER"],
  },

  {
    title: "Report Nilai Siswa",
    path: "/report-nilai",
    icon: FileProtectOutlined,
    roles: ["ROLE_OPERATOR", "ROLE_TEACHER"],
  },

  // {
  //   title: "Integrasi Dashboard",
  //   path: "/integrasi-ujian",
  //   icon: AppstoreOutlined,
  //   roles: ["ROLE_OPERATOR", "ROLE_TEACHER"],
  // },

  // {
  //   title: "Guru",
  //   path: "/lecture",
  //   icon: TeamOutlined,
  //   roles: ["ROLE_OPERATOR"],
  // },

  // {
  //   title: "Siswa",
  //   path: "/student",
  //   icon: TeamOutlined,
  //   roles: ["ROLE_OPERATOR"],
  // },
  // {
  //   title: "List Todo",
  //   path: "/list-todo-admin",
  //   icon: SolutionOutlined,
  //   roles: ["ROLE_OPERATOR"],
  // },
  // Lecture
  // {
  //   title: "Master Soal",
  //   icon: DatabaseOutlined,
  //   roles: ["ROLE_TEACHER", "ROLE_DUDI", "ROLE_OPERATOR"],
  //   children: [
  //     {
  //       title: "Kriteria Pertanyaan",
  //       path: "/question-criteria",
  //       icon: ApartmentOutlined,
  //       roles: ["ROLE_TEACHER", "ROLE_DUDI", "ROLE_OPERATOR"],
  //     },
  //     {
  //       title: "Tabel Nilai Linguistik",
  //       path: "/linguistic-value",
  //       icon: FileTextOutlined,
  //       roles: ["ROLE_TEACHER", "ROLE_DUDI", "ROLE_OPERATOR"],
  //     },
  //     {
  //       title: "Pengujian Soal",
  //       path: "/criteria-value",
  //       icon: FileSearchOutlined,
  //       // roles : ["ROLE_OPERATOR"],
  //       roles: ["ROLE_TEACHER", "ROLE_DUDI", "ROLE_OPERATOR"],
  //     },
  //   ],
  // },
  // {
  //   title: "List Todo",
  //   path: "/list-todo",
  //   icon: SolutionOutlined,
  //   roles: ["ROLE_TEACHER", "ROLE_DUDI"],
  // },
  {
    title: "Pengguna",
    path: "/user",
    icon: UsergroupAddOutlined,
    roles: ["ROLE_ADMINISTRATOR", "ROLE_OPERATOR"],
  },

  // {
  //   title: "Profil Sekolah",
  //   path: "/school-profile",
  //   icon: ApartmentOutlined,
  //   roles: ["ROLE_ADMINISTRATOR"],
  // },

  // {
  //   title: "RPS",
  //   path: "/rps",
  //   icon: RadarChartOutlined,
  //   roles: ["ROLE_TEACHER", "ROLE_DUDI", "ROLE_OPERATOR"],
  // },
  // {
  //   title: "Manajemen Soal",
  //   path: "/question",
  //   icon: FileSearchOutlined,
  //   roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER"],
  // },
  // {
  //   title: "Setting Ujian",
  //   path: "/setting-exam",
  //   icon: SolutionOutlined,
  //   roles: ["ROLE_OPERATOR"],
  // },
  // {
  //   title: "Setting Kuis",
  //   path: "/setting-quiz",
  //   icon: SolutionOutlined,
  //   roles: ["ROLE_TEACHER", "ROLE_DUDI", "ROLE_OPERATOR"],
  // },
  // {
  //   title: "Setting Latihan",
  //   path: "/setting-exercise",
  //   icon: SolutionOutlined,
  //   roles: ["ROLE_TEACHER", "ROLE_DUDI", "ROLE_OPERATOR"],
  // },
  // {
  //   title: "Ujian",
  //   path: "/exam",
  //   icon: SolutionOutlined,
  //   roles: ["ROLE_STUDENT"],
  // },
  // {
  //   title: "Kuis",
  //   path: "/quiz",
  //   icon: SolutionOutlined,
  //   roles: ["ROLE_STUDENT"],
  // },
  // {
  //   title: "Latihan",
  //   path: "/exercise",
  //   icon: SolutionOutlined,
  //   roles: ["ROLE_STUDENT"],
  // },
  // {
  //   title: "Nilai",
  //   path: "/result",
  //   icon: FileProtectOutlined,
  //   roles: ["ROLE_TEACHER", "ROLE_DUDI"],
  //   children: [
  //     {
  //       title: "Nilai Ujian",
  //       path: "/result/exam",
  //       icon: ContainerOutlined,
  //       roles: ["ROLE_TEACHER", "ROLE_DUDI"],
  //     },
  //     // {
  //     //   title: "Nilai Kuis",
  //     //   path: "/result/quiz",
  //     //   icon: ContainerOutlined,
  //     //   roles: ["ROLE_DUDI", "ROLE_TEACHER"],
  //     // },
  //     // {
  //     //   title: "Nilai Latihan",
  //     //   path: "/result/exercise",
  //     //   icon: ContainerOutlined,
  //     //   roles: ["ROLE_DUDI", "ROLE_TEACHER"],
  //     // },
  //   ],
  // },
  // {
  //   title: "Analisis & Laporan",
  //   icon: RadarChartOutlined,
  //   roles: ["ROLE_OPERATOR", "ROLE_TEACHER", "ROLE_ADMINISTRATOR"],
  //   children: [
  //     {
  //       title: "Analisis Peserta",
  //       path: "/ujian-analysis",
  //       icon: FileSearchOutlined,
  //       roles: ["ROLE_OPERATOR", "ROLE_TEACHER"],
  //     },
  //     {
  //       title: "Integrasi Data",
  //       path: "/integrasi-ujian",
  //       icon: AuditOutlined,
  //       roles: ["ROLE_OPERATOR", "ROLE_TEACHER"],
  //     },
  //     {
  //       title: "Laporan Nilai Siswa",
  //       path: "/laporan-nilai",
  //       icon: FileTextOutlined,
  //       roles: ["ROLE_OPERATOR", "ROLE_ADMINISTRATOR"],
  //     },
  //   ],
  // },
  // {
  //   title: "Tentang Penulis",
  //   path: "/about",
  //   icon: CopyrightOutlined,
  //   roles: ["ROLE_OPERATOR", "ROLE_DUDI", "ROLE_TEACHER", "ROLE_STUDENT"],
  // },

  // {
  //   title: "Bug收集",
  //   path: "/bug",
  //   icon: "bug",
  //   roles:["ROLE_OPERATOR"]
  // },
];
export default menuList;

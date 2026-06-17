import { rest } from "msw";
import { setupWorker } from "msw";
import loginAPI from "./login";
import remoteSearchAPI from "./remoteSearch";
import excelAPI from "./excel";
import tableAPI from "./table";
import monitor from "./monitor";

// Membuat handler untuk semua endpoint
const handlers = [
  // Login dan API pengguna
  rest.post("/login", (req, res, ctx) => res(ctx.json(loginAPI.login))),
  rest.post("/logout", (req, res, ctx) => res(ctx.json(loginAPI.logout))),
  rest.post("/userInfo", (req, res, ctx) => res(ctx.json(loginAPI.userInfo))),
  rest.get("/user/list", (req, res, ctx) => res(ctx.json(loginAPI.getUsers))),
  rest.post("/user/delete", (req, res, ctx) =>
    res(ctx.json(loginAPI.deleteUser))
  ),
  rest.post("/user/edit", (req, res, ctx) => res(ctx.json(loginAPI.editUser))),
  rest.post("/user/validatUserID", (req, res, ctx) =>
    res(ctx.json(loginAPI.ValidatUserID))
  ),
  rest.post("/user/add", (req, res, ctx) => res(ctx.json(loginAPI.addUser))),

  // Dashboard
  rest.get("/transaction/list", (req, res, ctx) =>
    res(ctx.json(remoteSearchAPI.transactionList))
  ),

  // Excel
  rest.get("/excel/list", (req, res, ctx) => res(ctx.json(excelAPI.excelList))),

  // Table
  rest.post("/table/list", (req, res, ctx) =>
    res(ctx.json(tableAPI.tableList))
  ),
  rest.post("/table/delete", (req, res, ctx) =>
    res(ctx.json(tableAPI.deleteItem))
  ),
  rest.post("/table/edit", (req, res, ctx) => res(ctx.json(tableAPI.editItem))),

  // Monitor
  rest.post("/monitor", (req, res, ctx) => res(ctx.json(monitor.monitor))),
];

// Setup MSW worker
const worker = setupWorker(...handlers);

// Mulai worker di development
worker.start();

export default worker;

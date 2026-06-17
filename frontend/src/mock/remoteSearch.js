import { rest } from "msw";
import { setupWorker } from "msw";
import { faker } from "@faker-js/faker";

const list = [];
const count = 20;

for (let i = 0; i < count; i++) {
  list.push({
    id: i,
    name: faker.name.findName(),
    email: faker.internet.email(),
    phone: faker.phone.phoneNumber(),
    address: faker.address.streetAddress(),
  });
}

const worker = setupWorker(
  rest.get("/api/remoteSearch", (req, res, ctx) => {
    return res(ctx.json(list));
  })
);

worker.start();

export default {
  remoteSearch: () => {
    return {
      code: 20000,
      data: { items: list },
    };
  },
};

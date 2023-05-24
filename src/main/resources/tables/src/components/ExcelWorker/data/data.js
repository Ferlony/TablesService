import json_table from "./table_data"; //assert { type: "json" };
const result = JSON.stringify(json_table)
export const recentSales = JSON.parse(result);


// [
//     {
//       id: 1,
//       country: "Canada",
//       soldBy: "Bill",
//       client: "Cerberus Corp.",
//       description: "Prothean artifacts",
//       value: 6250,
//       itemCount: 50
//     },
//     {
//       id: 10,
//       country: "UK",
//       soldBy: "Jill",
//       client: "General Products",
//       description: "Ion engines",
//       value: 33200,
//       itemCount: 40
//     }
//   ];

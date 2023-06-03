import json_table from "./table_data"; //assert { type: "json" };
const result = JSON.stringify(json_table)
export const recentTopics = JSON.parse(result);


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
// [
//     {
//       "id": "1",
//       "topic": "Что то про C",
//       "description": "abooba",
//       "user": "Ryan Gosling",
//       "userstatus": "Done",
//       "checked": "Yes",
//       "tabletitle": "Class A"
//     },
//     {
//       "id": "2",
//       "topic": "Что то про java",
//       "description": "abooba",
//       "user": "Christian Bale",
//       "userstatus": "Prog",
//       "checked": "No",
//       "tabletitle": "Class A"
//     },
// ]


// "ID": "id",
// "Topic": "topic",
// "Description": "description",
// "User": "user",
// "Userstatus": "userstatus",
// "Checked": "checked",
// "Tabletitle": "tabletitle"
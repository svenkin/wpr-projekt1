export default {
    getChapters :  () => {
                return new Promise((resolve,reject)=>{
                    fetch('https://jsonplaceholder.typicode.com/posts/1').then((response)=>{
                        // return response.json();
                        return {
                            data: [
                                {
                                    name: "Kapitel",
                                    description: "",
                                    sections: [
                                        {
                                            name: "Lektion 1",
                                            description : "lala",
                                            lections : [
                                              {
                                                name : "das",
                                                description: "lorem"
                                              }
                                            ],
                                            exam: {
                                              description: "lorem",
                                              questions: [
                                                {
                                                  type: "radio",
                                                  question: "lala ?"
                                                }
                                              ]
                                            }
                                        },
                                        {
                                            name: "Lektion 2"
                                        }
                                    ]
                                },
                                {
                                    name: "Kapitel 2",
                                    lections: [
                                        {
                                            name: "Lektion 1"
                                        },
                                        {
                                            name: "Lektion 2"
                                        }
                                    ]
                                }
                            ]
                        };
                        }).then((data)=>{
                            resolve(data);
                        });
                    })
                }
}

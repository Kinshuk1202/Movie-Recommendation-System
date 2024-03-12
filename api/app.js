const express = require('express');
const { spawn } = require('child_process');
const app = express();

app.use(express.json({ extended: true }));

app.get('/get/:movie', (req, res) => {
    const movie = req.params.movie;
    let recommendations = [];

    const pythonProcess = spawn('python', ['../script/script.py', movie]);

    pythonProcess.stdout.on('data', (data) => {
        recommendations = JSON.parse(data.toString());
    });

    pythonProcess.on('close', (code) => {
        console.log("code", code);
        res.json({ recommendations });
    });
});

app.listen(1202, () => {
    console.log("listening on port 1202");
});

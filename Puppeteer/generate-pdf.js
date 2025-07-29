const puppeteer = require('puppeteer');
const fs = require('fs');
const path = require('path');

(async () => {
    const browser = await puppeteer.launch({ headless: true });
    const page = await browser.newPage();

    const html = fs.readFileSync(path.join(__dirname, 'hello.html'), 'utf8');
    await page.setContent(html, { waitUntil: 'load' });

    await page.pdf({
        path: path.join(__dirname, 'output.pdf'),
        format: 'A4',
        printBackground: true,
    });

    console.log("PDF generated → output.pdf");

    await browser.close();
})();

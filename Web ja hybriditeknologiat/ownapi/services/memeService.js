export const fetchTemplates = async () => {
  const response = await fetch('https://api.memegen.link/templates');
  const data = await response.json();
  return data.map(template => template.id);
};

const getRandomTemplate = (templates) => {
  return templates[Math.floor(Math.random() * templates.length)];
};

export const generateMeme = async (templates, topText, bottomText) => {
  const randomTemplate = getRandomTemplate(templates);
  const response = await fetch(
    `https://api.memegen.link/images/${randomTemplate}/${encodeURIComponent(
      topText
    )}/${encodeURIComponent(bottomText)}.png`
  );
  return response.url;
};
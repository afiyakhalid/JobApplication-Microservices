// Mock Data Service Layer - Client-side only
// All data is stored in localStorage for demonstration purposes

interface Job {
  id: number;
  title: string;
  description: string;
  minSalary: number;
  maxSalary: number;
  location: string;
  companyId: number;
}

interface Company {
  id: number;
  name: string;
  description: string;
}

interface Review {
  id: number;
  title: string;
  description: string;
  rating: number;
  companyId: number;
}

// Initialize mock data
const initializeMockData = () => {
  if (!localStorage.getItem('jobs')) {
    const mockJobs: Job[] = [
      {
        id: 1,
        title: 'Senior Software Engineer',
        description: 'Lead development of scalable microservices architecture using modern technologies.',
        minSalary: 120000,
        maxSalary: 180000,
        location: 'San Francisco, CA',
        companyId: 1,
      },
      {
        id: 2,
        title: 'Product Manager',
        description: 'Drive product strategy and roadmap for our flagship products.',
        minSalary: 100000,
        maxSalary: 150000,
        location: 'New York, NY',
        companyId: 2,
      },
      {
        id: 3,
        title: 'UX Designer',
        description: 'Create beautiful and intuitive user experiences for our platform.',
        minSalary: 80000,
        maxSalary: 120000,
        location: 'Austin, TX',
        companyId: 1,
      },
    ];
    localStorage.setItem('jobs', JSON.stringify(mockJobs));
    localStorage.setItem('jobIdCounter', '4');
  }

  if (!localStorage.getItem('companies')) {
    const mockCompanies: Company[] = [
      {
        id: 1,
        name: 'TechCorp Solutions',
        description: 'Leading provider of enterprise software solutions, specializing in cloud infrastructure and AI-powered analytics.',
      },
      {
        id: 2,
        name: 'Innovation Labs',
        description: 'Fast-growing startup focused on revolutionizing the fintech industry with cutting-edge technology.',
      },
      {
        id: 3,
        name: 'Global Enterprises Inc.',
        description: 'Fortune 500 company offering diverse opportunities across multiple technology domains.',
      },
    ];
    localStorage.setItem('companies', JSON.stringify(mockCompanies));
    localStorage.setItem('companyIdCounter', '4');
  }

  if (!localStorage.getItem('reviews')) {
    const mockReviews: Review[] = [
      {
        id: 1,
        title: 'Excellent work culture',
        description: 'Great team collaboration and innovative projects. Management is supportive and encourages professional growth.',
        rating: 5,
        companyId: 1,
      },
      {
        id: 2,
        title: 'Good benefits package',
        description: 'Competitive salary and comprehensive benefits. Work-life balance could be better but overall positive experience.',
        rating: 4,
        companyId: 1,
      },
      {
        id: 3,
        title: 'Fast-paced environment',
        description: 'Exciting startup culture with lots of learning opportunities. Great for those who thrive in dynamic settings.',
        rating: 4,
        companyId: 2,
      },
    ];
    localStorage.setItem('reviews', JSON.stringify(mockReviews));
    localStorage.setItem('reviewIdCounter', '4');
  }
};

// Helper function to simulate async operations
const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

// Job Service
export const jobService = {
  async getAllJobs(): Promise<Job[]> {
    initializeMockData();
    await delay(300);
    const jobs = JSON.parse(localStorage.getItem('jobs') || '[]');
    return jobs;
  },

  async getJobById(id: number): Promise<Job> {
    await delay(200);
    const jobs = JSON.parse(localStorage.getItem('jobs') || '[]');
    const job = jobs.find((j: Job) => j.id === id);
    if (!job) throw new Error('Job not found');
    return job;
  },

  async createJob(job: Omit<Job, 'id'>): Promise<Job> {
    await delay(300);
    const jobs = JSON.parse(localStorage.getItem('jobs') || '[]');
    const counter = parseInt(localStorage.getItem('jobIdCounter') || '1');
    const newJob = { ...job, id: counter };
    jobs.push(newJob);
    localStorage.setItem('jobs', JSON.stringify(jobs));
    localStorage.setItem('jobIdCounter', (counter + 1).toString());
    return newJob;
  },

  async updateJob(id: number, job: Partial<Job>): Promise<Job> {
    await delay(300);
    const jobs = JSON.parse(localStorage.getItem('jobs') || '[]');
    const index = jobs.findIndex((j: Job) => j.id === id);
    if (index === -1) throw new Error('Job not found');
    jobs[index] = { ...jobs[index], ...job };
    localStorage.setItem('jobs', JSON.stringify(jobs));
    return jobs[index];
  },

  async deleteJob(id: number): Promise<boolean> {
    await delay(300);
    const jobs = JSON.parse(localStorage.getItem('jobs') || '[]');
    const filtered = jobs.filter((j: Job) => j.id !== id);
    localStorage.setItem('jobs', JSON.stringify(filtered));
    return true;
  },
};

// Company Service
export const companyService = {
  async getAllCompanies(): Promise<Company[]> {
    initializeMockData();
    await delay(300);
    const companies = JSON.parse(localStorage.getItem('companies') || '[]');
    return companies;
  },

  async getCompanyById(id: number): Promise<Company> {
    await delay(200);
    const companies = JSON.parse(localStorage.getItem('companies') || '[]');
    const company = companies.find((c: Company) => c.id === id);
    if (!company) throw new Error('Company not found');
    return company;
  },

  async createCompany(company: Omit<Company, 'id'>): Promise<Company> {
    await delay(300);
    const companies = JSON.parse(localStorage.getItem('companies') || '[]');
    const counter = parseInt(localStorage.getItem('companyIdCounter') || '1');
    const newCompany = { ...company, id: counter };
    companies.push(newCompany);
    localStorage.setItem('companies', JSON.stringify(companies));
    localStorage.setItem('companyIdCounter', (counter + 1).toString());
    return newCompany;
  },

  async updateCompany(id: number, company: Partial<Company>): Promise<Company> {
    await delay(300);
    const companies = JSON.parse(localStorage.getItem('companies') || '[]');
    const index = companies.findIndex((c: Company) => c.id === id);
    if (index === -1) throw new Error('Company not found');
    companies[index] = { ...companies[index], ...company };
    localStorage.setItem('companies', JSON.stringify(companies));
    return companies[index];
  },

  async deleteCompany(id: number): Promise<boolean> {
    await delay(300);
    const companies = JSON.parse(localStorage.getItem('companies') || '[]');
    const filtered = companies.filter((c: Company) => c.id !== id);
    localStorage.setItem('companies', JSON.stringify(filtered));
    return true;
  },
};

// Review Service
export const reviewService = {
  async getReviewsByCompany(companyId: number): Promise<Review[]> {
    initializeMockData();
    await delay(300);
    const reviews = JSON.parse(localStorage.getItem('reviews') || '[]');
    return reviews.filter((r: Review) => r.companyId === companyId);
  },

  async getReviewById(reviewId: number): Promise<Review> {
    await delay(200);
    const reviews = JSON.parse(localStorage.getItem('reviews') || '[]');
    const review = reviews.find((r: Review) => r.id === reviewId);
    if (!review) throw new Error('Review not found');
    return review;
  },

  async createReview(companyId: number, review: Omit<Review, 'id' | 'companyId'>): Promise<Review> {
    await delay(300);
    const reviews = JSON.parse(localStorage.getItem('reviews') || '[]');
    const counter = parseInt(localStorage.getItem('reviewIdCounter') || '1');
    const newReview = { ...review, id: counter, companyId };
    reviews.push(newReview);
    localStorage.setItem('reviews', JSON.stringify(reviews));
    localStorage.setItem('reviewIdCounter', (counter + 1).toString());
    return newReview;
  },

  async updateReview(reviewId: number, review: Partial<Review>): Promise<Review> {
    await delay(300);
    const reviews = JSON.parse(localStorage.getItem('reviews') || '[]');
    const index = reviews.findIndex((r: Review) => r.id === reviewId);
    if (index === -1) throw new Error('Review not found');
    reviews[index] = { ...reviews[index], ...review };
    localStorage.setItem('reviews', JSON.stringify(reviews));
    return reviews[index];
  },

  async deleteReview(reviewId: number): Promise<boolean> {
    await delay(300);
    const reviews = JSON.parse(localStorage.getItem('reviews') || '[]');
    const filtered = reviews.filter((r: Review) => r.id !== reviewId);
    localStorage.setItem('reviews', JSON.stringify(filtered));
    return true;
  },

  async getAverageRating(companyId: number): Promise<{ averageRating: number }> {
    await delay(200);
    const reviews = JSON.parse(localStorage.getItem('reviews') || '[]');
    const companyReviews = reviews.filter((r: Review) => r.companyId === companyId);
    if (companyReviews.length === 0) return { averageRating: 0 };
    const sum = companyReviews.reduce((acc: number, r: Review) => acc + r.rating, 0);
    return { averageRating: sum / companyReviews.length };
  },
};

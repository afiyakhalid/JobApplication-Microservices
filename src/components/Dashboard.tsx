import { useEffect, useState } from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from './ui/card';
import { Button } from './ui/button';
import { Briefcase, Building2, Star, TrendingUp } from 'lucide-react';
import { jobService, companyService } from './api/services';

interface DashboardProps {
  onNavigate: (page: string) => void;
}

export function Dashboard({ onNavigate }: DashboardProps) {
  const [stats, setStats] = useState({
    totalJobs: 0,
    totalCompanies: 0,
    totalReviews: 0,
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadStats();
  }, []);

  const loadStats = async () => {
    try {
      setLoading(true);
      const [jobs, companies] = await Promise.all([
        jobService.getAllJobs().catch(() => []),
        companyService.getAllCompanies().catch(() => []),
      ]);
      
      // Count all reviews across all companies
      let totalReviews = 0;
      const reviews = JSON.parse(localStorage.getItem('reviews') || '[]');
      totalReviews = reviews.length;
      
      setStats({
        totalJobs: jobs.length || 0,
        totalCompanies: companies.length || 0,
        totalReviews: totalReviews || 0,
      });
    } catch (error) {
      console.error('Error loading stats:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-8">
      {/* Welcome Section */}
      <div>
        <h1 className="text-4xl mb-2">Job Management Platform</h1>
        <p className="text-muted-foreground">Comprehensive solution for managing jobs, companies, and reviews</p>
      </div>

      {/* Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card className="hover:shadow-lg transition-all cursor-pointer border-2 hover:border-primary" onClick={() => onNavigate('jobs')}>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm">Total Jobs</CardTitle>
            <div className="h-10 w-10 rounded-lg bg-blue-100 dark:bg-blue-900/30 flex items-center justify-center">
              <Briefcase className="h-5 w-5 text-primary" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-3xl">{loading ? '...' : stats.totalJobs}</div>
            <p className="text-xs text-muted-foreground mt-1 flex items-center gap-1">
              <TrendingUp className="h-3 w-3" />
              Active job listings
            </p>
          </CardContent>
        </Card>

        <Card className="hover:shadow-lg transition-all cursor-pointer border-2 hover:border-primary" onClick={() => onNavigate('companies')}>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm">Total Companies</CardTitle>
            <div className="h-10 w-10 rounded-lg bg-green-100 dark:bg-green-900/30 flex items-center justify-center">
              <Building2 className="h-5 w-5 text-green-600 dark:text-green-400" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-3xl">{loading ? '...' : stats.totalCompanies}</div>
            <p className="text-xs text-muted-foreground mt-1 flex items-center gap-1">
              <TrendingUp className="h-3 w-3" />
              Registered companies
            </p>
          </CardContent>
        </Card>

        <Card className="hover:shadow-lg transition-all cursor-pointer border-2 hover:border-primary" onClick={() => onNavigate('reviews')}>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm">Total Reviews</CardTitle>
            <div className="h-10 w-10 rounded-lg bg-yellow-100 dark:bg-yellow-900/30 flex items-center justify-center">
              <Star className="h-5 w-5 text-yellow-600 dark:text-yellow-400" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-3xl">{loading ? '...' : stats.totalReviews}</div>
            <p className="text-xs text-muted-foreground mt-1 flex items-center gap-1">
              <TrendingUp className="h-3 w-3" />
              Company reviews
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Quick Actions */}
      <Card className="border-2">
        <CardHeader>
          <CardTitle>Quick Actions</CardTitle>
          <CardDescription>Navigate to different sections of the platform</CardDescription>
        </CardHeader>
        <CardContent className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Button 
            onClick={() => onNavigate('jobs')} 
            className="h-24 flex flex-col items-center justify-center gap-3"
            variant="outline"
          >
            <Briefcase className="h-8 w-8" />
            <span>Manage Jobs</span>
          </Button>
          <Button 
            onClick={() => onNavigate('companies')} 
            className="h-24 flex flex-col items-center justify-center gap-3"
            variant="outline"
          >
            <Building2 className="h-8 w-8" />
            <span>Manage Companies</span>
          </Button>
          <Button 
            onClick={() => onNavigate('reviews')} 
            className="h-24 flex flex-col items-center justify-center gap-3"
            variant="outline"
          >
            <Star className="h-8 w-8" />
            <span>Manage Reviews</span>
          </Button>
        </CardContent>
      </Card>

      {/* Platform Overview */}
      <Card className="border-2 bg-gradient-to-br from-primary/5 to-primary/10 dark:from-primary/10 dark:to-primary/20">
        <CardHeader>
          <CardTitle>Platform Overview</CardTitle>
          <CardDescription>Enterprise-grade job management solution</CardDescription>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <h3 className="mb-2 flex items-center gap-2">
                <div className="h-2 w-2 rounded-full bg-primary" />
                Core Features
              </h3>
              <ul className="text-sm text-muted-foreground space-y-2 ml-4">
                <li>• Comprehensive job posting management</li>
                <li>• Company profile administration</li>
                <li>• Review and rating system</li>
                <li>• Real-time data synchronization</li>
              </ul>
            </div>
            <div>
              <h3 className="mb-2 flex items-center gap-2">
                <div className="h-2 w-2 rounded-full bg-primary" />
                Platform Benefits
              </h3>
              <ul className="text-sm text-muted-foreground space-y-2 ml-4">
                <li>• Streamlined workflow management</li>
                <li>• Enhanced data organization</li>
                <li>• Professional interface design</li>
                <li>• Secure and reliable operations</li>
              </ul>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}

/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TestbedTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TestSuiteDetailComponent } from '../../../../../../main/webapp/app/entities/test-suite/test-suite-detail.component';
import { TestSuiteService } from '../../../../../../main/webapp/app/entities/test-suite/test-suite.service';
import { TestSuite } from '../../../../../../main/webapp/app/entities/test-suite/test-suite.model';

describe('Component Tests', () => {

    describe('TestSuite Management Detail Component', () => {
        let comp: TestSuiteDetailComponent;
        let fixture: ComponentFixture<TestSuiteDetailComponent>;
        let service: TestSuiteService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TestbedTestModule],
                declarations: [TestSuiteDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TestSuiteService,
                    JhiEventManager
                ]
            }).overrideTemplate(TestSuiteDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TestSuiteDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TestSuiteService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TestSuite(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.testSuite).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});

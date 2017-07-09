/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TestbedTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TestCaseDetailComponent } from '../../../../../../main/webapp/app/entities/test-case/test-case-detail.component';
import { TestCaseService } from '../../../../../../main/webapp/app/entities/test-case/test-case.service';
import { TestCase } from '../../../../../../main/webapp/app/entities/test-case/test-case.model';

describe('Component Tests', () => {

    describe('TestCase Management Detail Component', () => {
        let comp: TestCaseDetailComponent;
        let fixture: ComponentFixture<TestCaseDetailComponent>;
        let service: TestCaseService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TestbedTestModule],
                declarations: [TestCaseDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TestCaseService,
                    JhiEventManager
                ]
            }).overrideTemplate(TestCaseDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TestCaseDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TestCaseService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TestCase(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.testCase).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
